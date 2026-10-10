package com.backend_yireh_store.domain.port.in;

import com.backend_yireh_store.domain.model.SatisfactionSurvey;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.SurveyRequest;

import java.util.List;
import java.util.Optional;

public interface SurveyUseCase {

    SatisfactionSurvey submitSurvey(String userEmail, SurveyRequest request);

    Optional<SatisfactionSurvey> getByOrderId(Long orderId);

    List<SatisfactionSurvey> getAllSurveys();
}
