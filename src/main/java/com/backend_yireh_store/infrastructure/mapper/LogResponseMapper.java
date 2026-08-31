package com.backend_yireh_store.infrastructure.mapper;

import com.backend_yireh_store.domain.model.LogEntry;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.LogRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.LogResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LogResponseMapper {

    LogResponseMapper INSTANCE = Mappers.getMapper(LogResponseMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "level", source = "level")
    @Mapping(target = "source", source = "source")
    @Mapping(target = "tag", source = "tag")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "platform", source = "platform")
    @Mapping(target = "deviceInfo", source = "deviceInfo")
    @Mapping(target = "timestamp", source = "timestamp")
    LogResponse toResponse(LogEntry entry);

    @Mapping(target = "level", source = "level")
    @Mapping(target = "tag", source = "tag")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "stackTrace", source = "stackTrace")
    @Mapping(target = "platform", source = "platform")
    @Mapping(target = "deviceInfo", source = "deviceInfo")
    LogEntry toDomain(LogRequest request);
}