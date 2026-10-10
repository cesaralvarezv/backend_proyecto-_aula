package com.backend_yireh_store.domain.port.out;

import com.backend_yireh_store.domain.model.SatisfactionSurvey;

import java.util.List;
import java.util.Optional;

public interface SurveyRepositoryPort {

    SatisfactionSurvey save(SatisfactionSurvey survey);

    Optional<SatisfactionSurvey> findById(Long id);

    Optional<SatisfactionSurvey> findByOrderId(Long orderId);

    List<SatisfactionSurvey> findAll();

    boolean existsByOrderId(Long orderId);
}
