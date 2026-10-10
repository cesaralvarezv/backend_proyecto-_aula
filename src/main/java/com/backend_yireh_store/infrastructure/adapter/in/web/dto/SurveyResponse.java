package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponse {

    private Long id;
    private Long orderId;
    private String userEmail;
    private int rating;
    private String quickTags;
    private String comment;
    private LocalDateTime createdAt;
}
