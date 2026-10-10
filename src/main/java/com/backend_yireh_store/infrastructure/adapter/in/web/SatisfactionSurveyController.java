package com.backend_yireh_store.infrastructure.adapter.in.web;

import com.backend_yireh_store.domain.model.SatisfactionSurvey;
import com.backend_yireh_store.domain.port.in.SurveyUseCase;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.SurveyRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.SurveyResponse;
import com.backend_yireh_store.infrastructure.mapper.SatisfactionSurveyMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/satisfaction/surveys")
@CrossOrigin(origins = "*")
public class SatisfactionSurveyController {

    private final SurveyUseCase surveyUseCase;
    private final SatisfactionSurveyMapper mapper;

    public SatisfactionSurveyController(SurveyUseCase surveyUseCase, SatisfactionSurveyMapper mapper) {
        this.surveyUseCase = surveyUseCase;
        this.mapper = mapper;
    }

    /**
     * POST /api/satisfaction/surveys
     * Envío de calificación CSAT y tags rápidos tras la entrega del pedido.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SurveyResponse> submitSurvey(
            Authentication auth,
            @Valid @RequestBody SurveyRequest request) {

        String userEmail = auth.getName();
        SatisfactionSurvey survey = surveyUseCase.submitSurvey(userEmail, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(survey));
    }

    /**
     * GET /api/satisfaction/surveys/order/{orderId}
     * Consulta si una orden ya tiene encuesta registrada.
     */
    @GetMapping("/order/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SurveyResponse> getSurveyByOrder(@PathVariable Long orderId) {
        return surveyUseCase.getByOrderId(orderId)
                .map(s -> ResponseEntity.ok(mapper.toResponse(s)))
                .orElse(ResponseEntity.noContent().build());
    }

    /**
     * GET /api/satisfaction/surveys
     * Histórico global de valoraciones para panel administrativo Angular.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SurveyResponse>> getAllSurveys() {
        List<SurveyResponse> responses = surveyUseCase.getAllSurveys().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
