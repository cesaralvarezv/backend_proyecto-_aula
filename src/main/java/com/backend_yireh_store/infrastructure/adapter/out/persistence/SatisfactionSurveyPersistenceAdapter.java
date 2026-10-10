package com.backend_yireh_store.infrastructure.adapter.out.persistence;

import com.backend_yireh_store.domain.model.SatisfactionSurvey;
import com.backend_yireh_store.domain.port.out.SurveyRepositoryPort;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.SatisfactionSurveyEntity;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.repository.SatisfactionSurveyJpaRepository;
import com.backend_yireh_store.infrastructure.mapper.SatisfactionSurveyMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SatisfactionSurveyPersistenceAdapter implements SurveyRepositoryPort {

    private final SatisfactionSurveyJpaRepository jpaRepository;
    private final SatisfactionSurveyMapper mapper;

    public SatisfactionSurveyPersistenceAdapter(SatisfactionSurveyJpaRepository jpaRepository, SatisfactionSurveyMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public SatisfactionSurvey save(SatisfactionSurvey survey) {
        SatisfactionSurveyEntity entity = mapper.toEntity(survey);
        SatisfactionSurveyEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<SatisfactionSurvey> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<SatisfactionSurvey> findByOrderId(Long orderId) {
        return jpaRepository.findByOrderId(orderId).map(mapper::toDomain);
    }

    @Override
    public List<SatisfactionSurvey> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByOrderId(Long orderId) {
        return jpaRepository.existsByOrderId(orderId);
    }
}
