package com.backend_yireh_store.domain.port.out;

import com.backend_yireh_store.domain.model.ChatMessage;
import java.util.List;

public interface LlmClientPort {
    /**
     * Envía el prompt del sistema, el historial previo y la nueva consulta del usuario al LLM (Ollama).
     *
     * @param systemPrompt Directivas del sistema y contexto de la tienda
     * @param history Historial de mensajes previos de la conversación
     * @param userMessage Nuevo mensaje enviado por el usuario
     * @return Respuesta generada por el LLM
     */
    String generateResponse(String systemPrompt, List<ChatMessage> history, String userMessage);

    /**
     * Verifica la disponibilidad de la conexión con el servidor Ollama y el modelo configurado.
     *
     * @return true si Ollama responde correctamente, false en caso contrario
     */
    boolean isAvailable();

    /**
     * Retorna el nombre del modelo configurado.
     */
    String getModelName();
}
