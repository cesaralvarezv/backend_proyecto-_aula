package com.backend_yireh_store.infrastructure.adapter.out.persistence.repository;

import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpringLogJpaRepository extends JpaRepository<LogEntity, Long> {
    List<LogEntity> findTopNByOrderByTimestampDesc(int n);
    List<LogEntity> findTopNBySourceOrderByTimestampDesc(String source, int n);
    List<LogEntity> findTopNByLevelOrderByTimestampDesc(String level, int n);
}
