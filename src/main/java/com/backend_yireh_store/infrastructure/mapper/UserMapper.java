package com.backend_yireh_store.infrastructure.mapper;

import com.backend_yireh_store.domain.model.RoleName;
import com.backend_yireh_store.domain.model.User;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.UserResponse;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // Entity → Domain
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "passwordHash", source = "passwordHash")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "createdAt", source = "createdAt")
    User toDomain(UserEntity entity);

    // Domain → Entity
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "passwordHash", source = "passwordHash")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "createdAt", source = "createdAt")
    UserEntity toEntity(User domain);

    // Domain → Response DTO
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStrings")
    UserResponse toResponse(User domain);

    @Named("rolesToStrings")
    static Set<String> rolesToStrings(Set<RoleName> roles) {
        if (roles == null) return Set.of();
        return roles.stream().map(Enum::name).collect(Collectors.toSet());
    }
}