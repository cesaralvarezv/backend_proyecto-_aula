package com.backend_yireh_store.infrastructure.adapter.in.web;

import com.backend_yireh_store.domain.model.LogEntry;
import com.backend_yireh_store.domain.port.in.LogUseCase;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.LogRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.LogResponse;
import com.backend_yireh_store.infrastructure.mapper.LogResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@CrossOrigin(origins = "*")
public class LogController {

    private final LogUseCase logUseCase;
    private final LogResponseMapper mapper;

    public LogController(LogUseCase logUseCase, LogResponseMapper mapper) {
        this.logUseCase = logUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<LogResponse> createLog(@Valid @RequestBody LogRequest req) {
        LogEntry entry = logUseCase.registerFrontend(
                req.level(), req.tag(), req.message(),
                req.stackTrace(), req.platform(), req.deviceInfo());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(entry));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<LogResponse>> createLogs(@Valid @RequestBody List<LogRequest> reqs) {
        List<LogResponse> responses = reqs.stream()
                .map(req -> {
                    LogEntry entry = logUseCase.registerFrontend(
                            req.level(), req.tag(), req.message(),
                            req.stackTrace(), req.platform(), req.deviceInfo());
                    return mapper.toResponse(entry);
                })
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @GetMapping
    public ResponseEntity<List<LogResponse>> getLogs(
            @RequestParam(defaultValue = "100") int limit) {
        return ResponseEntity.ok(logUseCase.getAll(limit).stream()
                .map(mapper::toResponse).toList());
    }

    @GetMapping("/{source}")
    public ResponseEntity<List<LogResponse>> getLogsBySource(
            @PathVariable String source,
            @RequestParam(defaultValue = "100") int limit) {
        return ResponseEntity.ok(logUseCase.getBySource(source.toUpperCase(), limit).stream()
                .map(mapper::toResponse).toList());
    }
}