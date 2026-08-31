package com.backend_yireh_store.infrastructure.mapper;

import com.backend_yireh_store.domain.model.LogEntry;
import com.backend_yireh_store.infrastructure.adapter.out.persistence.entity.LogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LogMapper {

    LogMapper INSTANCE = Mappers.getMapper(LogMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "level", source = "level")
    @Mapping(target = "source", source = "source")
    @Mapping(target = "tag", source = "tag")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "stackTrace", source = "stackTrace")
    @Mapping(target = "platform", source = "platform")
    @Mapping(target = "deviceInfo", source = "deviceInfo")
    @Mapping(target = "timestamp", source = "timestamp")
    LogEntry toDomain(LogEntity entity);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "level", source = "level")
    @Mapping(target = "source", source = "source")
    @Mapping(target = "tag", source = "tag")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "stackTrace", source = "stackTrace")
    @Mapping(target = "platform", source = "platform")
    @Mapping(target = "deviceInfo", source = "deviceInfo")
    @Mapping(target = "timestamp", source = "timestamp")
    LogEntity toEntity(LogEntry domain);
}