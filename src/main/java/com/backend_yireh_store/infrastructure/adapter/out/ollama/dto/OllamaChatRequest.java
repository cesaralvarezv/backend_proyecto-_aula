package com.backend_yireh_store.infrastructure.adapter.out.ollama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OllamaChatRequest {
    private String model;
    private List<OllamaMessageDto> messages;
    @Builder.Default
    private boolean stream = false;
}
