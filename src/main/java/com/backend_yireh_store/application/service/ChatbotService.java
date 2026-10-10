package com.backend_yireh_store.application.service;

import com.backend_yireh_store.domain.model.ChatMessage;
import com.backend_yireh_store.domain.model.Product;
import com.backend_yireh_store.domain.port.in.ChatbotUseCase;
import com.backend_yireh_store.domain.port.out.LlmClientPort;
import com.backend_yireh_store.domain.port.out.ProductRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatbotService implements ChatbotUseCase {

    private static final Logger log = LoggerFactory.getLogger(ChatbotService.class);
    private static final int MAX_HISTORY_MESSAGES = 10;

    private final LlmClientPort llmClientPort;
    private final ProductRepositoryPort productRepositoryPort;

    // Almacenamiento en memoria del historial de conversaciones por sesión
    private final Map<String, List<ChatMessage>> conversationSessions = new ConcurrentHashMap<>();

    public ChatbotService(LlmClientPort llmClientPort, ProductRepositoryPort productRepositoryPort) {
        this.llmClientPort = llmClientPort;
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public ChatMessage sendMessage(String sessionId, String message) {
        String safeSessionId = (sessionId == null || sessionId.trim().isEmpty())
                ? UUID.randomUUID().toString()
                : sessionId.trim();

        List<ChatMessage> history = conversationSessions.computeIfAbsent(
                safeSessionId, k -> Collections.synchronizedList(new ArrayList<>())
        );

        // 1. Construir System Prompt contextualizado con los productos reales de Yireh Store
        String systemPrompt = buildStoreSystemPrompt();

        // 2. Preparar snapshot del historial (últimos MAX_HISTORY_MESSAGES)
        List<ChatMessage> historySnapshot;
        synchronized (history) {
            int fromIndex = Math.max(0, history.size() - MAX_HISTORY_MESSAGES);
            historySnapshot = new ArrayList<>(history.subList(fromIndex, history.size()));
        }

        // 3. Consultar al LLM a través del puerto
        String responseContent = llmClientPort.generateResponse(systemPrompt, historySnapshot, message);

        // 4. Registrar mensajes en el historial
        ChatMessage userMsg = ChatMessage.user(message);
        ChatMessage assistantMsg = ChatMessage.assistant(responseContent);

        synchronized (history) {
            history.add(userMsg);
            history.add(assistantMsg);
        }

        log.debug("Mensaje procesado para sesión {}. Historial total: {}", safeSessionId, history.size());
        return assistantMsg;
    }

    @Override
    public List<ChatMessage> getHistory(String sessionId) {
        if (sessionId == null) return Collections.emptyList();
        List<ChatMessage> history = conversationSessions.get(sessionId.trim());
        if (history == null) return Collections.emptyList();

        synchronized (history) {
            return new ArrayList<>(history);
        }
    }

    @Override
    public void clearSession(String sessionId) {
        if (sessionId != null) {
            conversationSessions.remove(sessionId.trim());
            log.info("Sesión de chatbot eliminada: {}", sessionId);
        }
    }

    @Override
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new LinkedHashMap<>();
        status.put("llmAvailable", llmClientPort.isAvailable());
        status.put("model", llmClientPort.getModelName());
        status.put("activeSessions", conversationSessions.size());
        status.put("timestamp", LocalDateTime.now());
        return status;
    }

    /**
     * Construye un prompt del sistema rico en contexto del dominio de Yireh Store,
     * inyectando dinámicamente los productos activos de la base de datos.
     */
    private String buildStoreSystemPrompt() {
        StringBuilder sb = new StringBuilder();
        sb.append("Eres 'YirehBot', el asistente virtual inteligente y cordial de 'Yireh Store', una tienda y cafetería moderna.\n");
        sb.append("Tu misión es asesorar a los clientes, responder preguntas sobre los productos, precios, disponibilidad y guiarlos en su compra.\n\n");
        sb.append("REGLAS DE COMPORTAMIENTO:\n");
        sb.append("1. Responde siempre en español, de forma cálida, amable, concisa y profesional.\n");
        sb.append("2. Utiliza la lista de productos disponibles a continuación para responder sobre precios y stock.\n");
        sb.append("3. Si un producto no está en el catálogo, indícale amablemente que no está disponible por el momento.\n");
        sb.append("4. Métodos de pago aceptados en Yireh Store: Efectivo, Tarjeta y Billetera Virtual (saldo de cuenta).\n");
        sb.append("5. Si te preguntan sobre el estado de un pedido o factura, invítalos a revisar su panel de usuario o sección de Pedidos.\n\n");
        sb.append("CATÁLOGO ACTUAL DE PRODUCTOS ACTIVOS:\n");

        try {
            List<Product> products = productRepositoryPort.findByActiveTrue();
            if (products == null || products.isEmpty()) {
                sb.append("- Actualmente no hay productos registrados en el catálogo.\n");
            } else {
                for (Product p : products) {
                    sb.append(String.format("- %s | Precio: $%s COP | Stock disp: %d | Categoría: %s | %s\n",
                            p.getName(),
                            p.getPrice() != null ? p.getPrice().toPlainString() : "N/A",
                            p.getQuantity(),
                            (p.getCategory() != null && p.getCategory().getName() != null) ? p.getCategory().getName() : "General",
                            p.getDescription() != null ? p.getDescription() : ""));
                }
            }
        } catch (Exception e) {
            log.warn("No se pudieron cargar productos dinámicos para el prompt: {}", e.getMessage());
            sb.append("- Variedad de café, bebidas calientes y frías, repostería y snacks.\n");
        }

        return sb.toString();
    }
}
