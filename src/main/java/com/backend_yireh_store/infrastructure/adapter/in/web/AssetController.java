package com.backend_yireh_store.infrastructure.adapter.in.web;

import com.backend_yireh_store.domain.model.PhysicalAsset;
import com.backend_yireh_store.domain.port.in.AssetUseCase;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.AssetRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.AssetResponse;
import com.backend_yireh_store.infrastructure.mapper.PhysicalAssetMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assets")
@CrossOrigin(origins = "*")
public class AssetController {

    private final AssetUseCase assetUseCase;
    private final PhysicalAssetMapper mapper;

    public AssetController(AssetUseCase assetUseCase, PhysicalAssetMapper mapper) {
        this.assetUseCase = assetUseCase;
        this.mapper = mapper;
    }

    /**
     * GET /api/assets
     * Lista completa de activos de cocina y maquinaria.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    public ResponseEntity<List<AssetResponse>> getAllAssets() {
        List<AssetResponse> responses = assetUseCase.getAllAssets().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/assets/{id}
     * Detalle técnico de un activo.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    public ResponseEntity<AssetResponse> getAssetById(@PathVariable Long id) {
        PhysicalAsset asset = assetUseCase.getById(id);
        return ResponseEntity.ok(mapper.toResponse(asset));
    }

    /**
     * POST /api/assets
     * Registro de nuevo equipo o mobiliario.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssetResponse> createAsset(@Valid @RequestBody AssetRequest request) {
        PhysicalAsset asset = assetUseCase.createAsset(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(asset));
    }

    /**
     * PUT /api/assets/{id}
     * Actualización de datos o mantenimiento de un activo.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssetResponse> updateAsset(@PathVariable Long id,
                                                     @Valid @RequestBody AssetRequest request) {
        PhysicalAsset asset = assetUseCase.updateAsset(id, request);
        return ResponseEntity.ok(mapper.toResponse(asset));
    }

    /**
     * DELETE /api/assets/{id}
     * Eliminación de un activo.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        assetUseCase.deleteAsset(id);
        return ResponseEntity.noContent().build();
    }
}
