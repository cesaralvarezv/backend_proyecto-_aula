package com.backend_yireh_store.application.service;

import com.backend_yireh_store.domain.model.Order;
import com.backend_yireh_store.domain.model.OrderStatus;
import com.backend_yireh_store.domain.model.SatisfactionSurvey;
import com.backend_yireh_store.domain.port.in.SurveyUseCase;
import com.backend_yireh_store.domain.port.out.OrderRepositoryPort;
import com.backend_yireh_store.domain.port.out.SurveyRepositoryPort;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.SurveyRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SatisfactionSurveyService implements SurveyUseCase {

    private final SurveyRepositoryPort surveyRepository;
    private final OrderRepositoryPort orderRepository;

    public SatisfactionSurveyService(SurveyRepositoryPort surveyRepository, OrderRepositoryPort orderRepository) {
        this.surveyRepository = surveyRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public SatisfactionSurvey submitSurvey(String userEmail, SurveyRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la orden con ID: " + request.getOrderId()));

        // Validar que el pedido esté entregado
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new IllegalStateException("Solo se pueden calificar pedidos que ya hayan sido entregados (estado DELIVERED). Estado actual: " + order.getStatus());
        }

        // Validar que no exista encuesta previa para esta orden
        if (surveyRepository.existsByOrderId(order.getId())) {
            throw new IllegalStateException("Ya se registró una encuesta de satisfacción para la orden #" + order.getOrderNumber());
        }

        SatisfactionSurvey survey = SatisfactionSurvey.create(
                order.getId(),
                userEmail,
                request.getRating(),
                request.getQuickTags(),
                request.getComment()
        );

        return surveyRepository.save(survey);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SatisfactionSurvey> getByOrderId(Long orderId) {
        return surveyRepository.findByOrderId(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SatisfactionSurvey> getAllSurveys() {
        return surveyRepository.findAll();
    }
}
