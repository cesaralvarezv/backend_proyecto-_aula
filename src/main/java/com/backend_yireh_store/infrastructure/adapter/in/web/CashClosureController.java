package com.backend_yireh_store.infrastructure.adapter.in.web;

import com.backend_yireh_store.domain.model.CashClosure;
import com.backend_yireh_store.domain.port.in.CashClosureUseCase;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.CashClosureResponse;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.CloseCashClosureRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.OpenCashClosureRequest;
import com.backend_yireh_store.infrastructure.mapper.CashClosureMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cash-closures")
@CrossOrigin(origins = "*")
public class CashClosureController {

    private final CashClosureUseCase cashClosureUseCase;
    private final CashClosureMapper mapper;

    public CashClosureController(CashClosureUseCase cashClosureUseCase, CashClosureMapper mapper) {
        this.cashClosureUseCase = cashClosureUseCase;
        this.mapper = mapper;
    }

    /**
     * POST /api/cash-closures/open
     * Apertura de turno con monto base en efectivo.
     */
    @PostMapping("/open")
    @PreAuthorize("hasAnyRole('CASHIER', 'ADMIN')")
    public ResponseEntity<CashClosureResponse> openClosure(
            Authentication auth,
            @Valid @RequestBody OpenCashClosureRequest request) {

        String email = auth.getName();
        CashClosure closure = cashClosureUseCase.openClosure(email, request.getOpeningAmount(), request.getNotes());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(closure));
    }

    /**
     * POST /api/cash-closures/close
     * Cierre formal de caja con arqueo de dinero (declaración física y cálculo de descuadre).
     */
    @PostMapping("/close")
    @PreAuthorize("hasAnyRole('CASHIER', 'ADMIN')")
    public ResponseEntity<CashClosureResponse> closeClosure(
            Authentication auth,
            @Valid @RequestBody CloseCashClosureRequest request) {

        String email = auth.getName();
        CashClosure closure = cashClosureUseCase.closeClosure(email, request.getDeclaredCash(), request.getNotes());
        return ResponseEntity.ok(mapper.toResponse(closure));
    }

    /**
     * GET /api/cash-closures/current
     * Consulta el turno actualmente abierto para el cajero o el sistema.
     */
    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('CASHIER', 'ADMIN')")
    public ResponseEntity<CashClosureResponse> getCurrentClosure(Authentication auth) {
        String email = auth.getName();
        return cashClosureUseCase.getActiveClosure(email)
                .map(closure -> ResponseEntity.ok(mapper.toResponse(closure)))
                .orElse(ResponseEntity.noContent().build());
    }

    /**
     * GET /api/cash-closures/{id}
     * Detalle de un acta de cierre de caja específica.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CASHIER', 'ADMIN')")
    public ResponseEntity<CashClosureResponse> getById(@PathVariable Long id) {
        CashClosure closure = cashClosureUseCase.getById(id);
        return ResponseEntity.ok(mapper.toResponse(closure));
    }

    /**
     * GET /api/cash-closures
     * Histórico consolidado de turnos y actas de cierre (exclusivo administración).
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CashClosureResponse>> getAll() {
        List<CashClosureResponse> responses = cashClosureUseCase.getAllClosures().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
