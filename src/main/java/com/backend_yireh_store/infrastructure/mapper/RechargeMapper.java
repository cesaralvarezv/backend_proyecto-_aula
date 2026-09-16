package com.backend_yireh_store.infrastructure.mapper;

import com.backend_yireh_store.domain.model.Recharge;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.RechargeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RechargeMapper {
    Recharge toDomain(RechargeEntity entity);
    RechargeEntity toEntity(Recharge domain);
}
