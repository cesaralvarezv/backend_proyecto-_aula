package com.backend_yireh_store.domain.port.in;

import com.backend_yireh_store.domain.model.ChatMessage;
import java.util.List;
import java.util.Map;

public interface ChatbotUseCase {
    /**
     * Procesa un mensaje de usuario en una sesión dada y genera la respuesta del asistente.
     */
    ChatMessage sendMessage(String sessionId, String message);

    /**
     * Obtiene el historial de mensajes de una conversación.
     */
    List<ChatMessage> getHistory(String sessionId);

    /**
     * Limpia la memoria de conversación de una sesión.
     */
    void clearSession(String sessionId);

    /**
     * Obtiene el estado del servicio de IA (Ollama) y el modelo configurado.
     */
    Map<String, Object> getStatus();
}
