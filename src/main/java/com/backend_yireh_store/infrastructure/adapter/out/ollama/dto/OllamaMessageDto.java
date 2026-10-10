package com.backend_yireh_store.infrastructure.adapter.out.ollama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OllamaMessageDto {
    private String role;
    private String content;
}
