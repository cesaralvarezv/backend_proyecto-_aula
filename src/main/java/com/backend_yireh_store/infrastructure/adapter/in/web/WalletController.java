package com.backend_yireh_store.infrastructure.adapter.in.web;

import com.backend_yireh_store.domain.model.Recharge;
import com.backend_yireh_store.domain.port.in.WalletUseCase;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.RechargeRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.RechargeResponse;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.WalletBalanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@CrossOrigin(origins = "*")
public class WalletController {

    private final WalletUseCase walletUseCase;

    public WalletController(WalletUseCase walletUseCase) {
        this.walletUseCase = walletUseCase;
    }

    /** GET /api/wallet/balance — saldo del usuario autenticado. */
    @GetMapping("/balance")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WalletBalanceResponse> getBalance(Authentication auth) {
        String email = auth.getName();
        return ResponseEntity.ok(new WalletBalanceResponse(email, walletUseCase.getBalance(email)));
    }

    /** POST /api/wallet/recharge — recargar saldo. */
    @PostMapping("/recharge")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RechargeResponse> recharge(Authentication auth,
                                                      @RequestBody RechargeRequest request) {
        String email = auth.getName();
        Recharge recharge = walletUseCase.recharge(email, request.amount(),
                request.paymentMethod(), request.referenceCode());
        return ResponseEntity.ok(toResponse(recharge));
    }

    /** GET /api/wallet/transactions — historial de movimientos. */
    @GetMapping("/transactions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RechargeResponse>> getTransactions(Authentication auth) {
        String email = auth.getName();
        List<RechargeResponse> responses = walletUseCase.getTransactions(email)
                .stream().map(this::toResponse).toList();
        return ResponseEntity.ok(responses);
    }

    private RechargeResponse toResponse(Recharge r) {
        return new RechargeResponse(r.getId(), r.getUserId(), r.getAmount(),
                r.getPaymentMethod(), r.getStatus(), r.getReferenceCode(), r.getCreatedAt());
    }
}
