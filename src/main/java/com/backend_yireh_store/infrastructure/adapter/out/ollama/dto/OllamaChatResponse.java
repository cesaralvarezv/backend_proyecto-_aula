package com.backend_yireh_store.infrastructure.adapter.out.ollama.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OllamaChatResponse {
    private String model;
    private OllamaMessageDto message;
    private boolean done;
}
