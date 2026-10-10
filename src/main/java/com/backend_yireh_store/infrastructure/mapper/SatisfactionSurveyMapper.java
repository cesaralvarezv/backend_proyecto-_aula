package com.backend_yireh_store.infrastructure.mapper;

import com.backend_yireh_store.domain.model.SatisfactionSurvey;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.SurveyResponse;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.SatisfactionSurveyEntity;
import org.springframework.stereotype.Component;

@Component
public class SatisfactionSurveyMapper {

    public SatisfactionSurvey toDomain(SatisfactionSurveyEntity entity) {
        if (entity == null) return null;
        return SatisfactionSurvey.builder()
                .id(entity.getId())
                .orderId(entity.getOrderId())
                .userEmail(entity.getUserEmail())
                .rating(entity.getRating())
                .quickTags(entity.getQuickTags())
                .comment(entity.getComment())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public SatisfactionSurveyEntity toEntity(SatisfactionSurvey domain) {
        if (domain == null) return null;
        return SatisfactionSurveyEntity.builder()
                .id(domain.getId())
                .orderId(domain.getOrderId())
                .userEmail(domain.getUserEmail())
                .rating(domain.getRating())
                .quickTags(domain.getQuickTags())
                .comment(domain.getComment())
                .createdAt(domain.getCreatedAt())
                .build();
    }

    public SurveyResponse toResponse(SatisfactionSurvey domain) {
        if (domain == null) return null;
        return SurveyResponse.builder()
                .id(domain.getId())
                .orderId(domain.getOrderId())
                .userEmail(domain.getUserEmail())
                .rating(domain.getRating())
                .quickTags(domain.getQuickTags())
                .comment(domain.getComment())
                .createdAt(domain.getCreatedAt())
                .build();
    }
}
