package com.backend_yireh_store.infrastructure.mapper;

import com.backend_yireh_store.domain.model.Category;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.CategoryRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.CategoryResponse;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    // Entity → Domain
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "icon", source = "icon")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    Category toDomain(CategoryEntity entity);

    // Domain → Entity
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "icon", source = "icon")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    CategoryEntity toEntity(Category domain);

    // Domain → Response DTO
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "icon", source = "icon")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    CategoryResponse toResponse(Category domain);

    // Request DTO → Domain (for create)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "icon", source = "icon")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toDomainFromRequest(CategoryRequest request);

    // Request DTO → Domain (for update) - returns updated domain
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "icon", source = "icon")
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "order", source = "order")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category updateDomainFromRequest(CategoryRequest request, @MappingTarget Category domain);
}