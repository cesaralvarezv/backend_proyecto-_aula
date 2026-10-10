package com.backend_yireh_store.infrastructure.mapper;

import com.backend_yireh_store.domain.model.Category;
import com.backend_yireh_store.domain.model.Product;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.ProductRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.ProductResponse;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    // Entity → Domain
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryEntityToDomain")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    Product toDomain(ProductEntity entity);

    // Domain → Entity
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryDomainToEntity")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ProductEntity toEntity(Product domain);

    // Domain → Response DTO
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ProductResponse toResponse(Product domain);

    // Request DTO → Domain (for create)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "order", source = "order")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toDomainFromRequest(ProductRequest request);

    // Request DTO → Domain (for update) - returns updated domain
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "imageUrl", source = "imageUrl")
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "order", source = "order")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product updateDomainFromRequest(ProductRequest request, @MappingTarget Product domain);

    @Named("categoryEntityToDomain")
    static Category categoryEntityToDomain(CategoryEntity entity) {
        if (entity == null) return null;
        return Category.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .icon(entity.getIcon())
                .location(entity.getLocation())
                .active(entity.isActive())
                .order(entity.getOrder())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Named("categoryDomainToEntity")
    static CategoryEntity categoryDomainToEntity(Category domain) {
        if (domain == null) return null;
        CategoryEntity entity = new CategoryEntity();
        entity.setId(domain.getId());
        return entity;
    }
}