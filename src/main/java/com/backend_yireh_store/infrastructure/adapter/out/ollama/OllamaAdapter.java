package com.backend_yireh_store.infrastructure.adapter.out.ollama;

import com.backend_yireh_store.domain.model.ChatMessage;
import com.backend_yireh_store.domain.port.out.LlmClientPort;
import com.backend_yireh_store.infrastructure.adapter.out.ollama.dto.OllamaChatRequest;
import com.backend_yireh_store.infrastructure.adapter.out.ollama.dto.OllamaChatResponse;
import com.backend_yireh_store.infrastructure.adapter.out.ollama.dto.OllamaMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
public class OllamaAdapter implements LlmClientPort {

    private static final Logger log = LoggerFactory.getLogger(OllamaAdapter.class);

    private final String baseUrl;
    private final String model;
    private final RestClient restClient;

    public OllamaAdapter(
            @Value("${ollama.base-url:http://127.0.0.1:11434}") String baseUrl,
            @Value("${ollama.model:qwen2.5-coder:7b}") String model) {
        this.baseUrl = baseUrl.replaceAll("/+$", "");
        this.model = model;

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(15));
        factory.setReadTimeout(Duration.ofSeconds(120));

        this.restClient = RestClient.builder()
                .baseUrl(this.baseUrl)
                .requestFactory(factory)
                .build();

        log.info("OllamaAdapter inicializado con URL: {} y Modelo: {}", this.baseUrl, this.model);
    }

    @Override
    public String generateResponse(String systemPrompt, List<ChatMessage> history, String userMessage) {
        List<OllamaMessageDto> messages = new ArrayList<>();

        if (systemPrompt != null && !systemPrompt.trim().isEmpty()) {
            messages.add(new OllamaMessageDto("system", systemPrompt.trim()));
        }

        if (history != null) {
            for (ChatMessage msg : history) {
                if (msg != null && msg.getContent() != null && !msg.getContent().trim().isEmpty()) {
                    messages.add(new OllamaMessageDto(msg.getRole(), msg.getContent()));
                }
            }
        }

        if (userMessage != null && !userMessage.trim().isEmpty()) {
            messages.add(new OllamaMessageDto("user", userMessage.trim()));
        }

        OllamaChatRequest request = OllamaChatRequest.builder()
                .model(this.model)
                .messages(messages)
                .stream(false)
                .build();

        try {
            OllamaChatResponse response = restClient.post()
                    .uri("/api/chat")
                    .body(request)
                    .retrieve()
                    .body(OllamaChatResponse.class);

            if (response != null && response.getMessage() != null && response.getMessage().getContent() != null) {
                return response.getMessage().getContent().trim();
            }
            return "Lo siento, no pude procesar una respuesta en este momento.";
        } catch (Exception e) {
            log.error("Error al comunicarse con Ollama ({}): {}", this.baseUrl, e.getMessage(), e);
            return "En este momento el servicio local de Inteligencia Artificial (Ollama) no está disponible o está iniciando. Por favor asegúrate de ejecutar 'ollama serve' en tu equipo.";
        }
    }

    @Override
    public boolean isAvailable() {
        try {
            restClient.get()
                    .uri("/api/tags")
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getModelName() {
        return this.model;
    }
}
