package com.backend_yireh_store.infrastructure.adapter.in.web;

import com.backend_yireh_store.domain.model.ChatMessage;
import com.backend_yireh_store.domain.port.in.ChatbotUseCase;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.ChatRequestDto;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.ChatResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatbotController {

    private final ChatbotUseCase chatbotUseCase;

    public ChatbotController(ChatbotUseCase chatbotUseCase) {
        this.chatbotUseCase = chatbotUseCase;
    }

    @PostMapping
    public ResponseEntity<ChatResponseDto> sendMessage(@Valid @RequestBody ChatRequestDto request) {
        String sessionId = (request.sessionId() != null && !request.sessionId().trim().isEmpty())
                ? request.sessionId().trim()
                : UUID.randomUUID().toString();

        ChatMessage assistantResponse = chatbotUseCase.sendMessage(sessionId, request.message());

        ChatResponseDto responseDto = new ChatResponseDto(
                sessionId,
                assistantResponse.getRole(),
                assistantResponse.getContent(),
                assistantResponse.getTimestamp()
        );

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/history/{sessionId}")
    public ResponseEntity<List<ChatResponseDto>> getHistory(@PathVariable String sessionId) {
        List<ChatMessage> history = chatbotUseCase.getHistory(sessionId);
        List<ChatResponseDto> responseDtos = history.stream()
                .map(msg -> new ChatResponseDto(sessionId, msg.getRole(), msg.getContent(), msg.getTimestamp()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<Map<String, String>> clearSession(@PathVariable String sessionId) {
        chatbotUseCase.clearSession(sessionId);
        return ResponseEntity.ok(Map.of("message", "Sesión de chat reiniciada correctamente", "sessionId", sessionId));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        return ResponseEntity.ok(chatbotUseCase.getStatus());
    }
}
