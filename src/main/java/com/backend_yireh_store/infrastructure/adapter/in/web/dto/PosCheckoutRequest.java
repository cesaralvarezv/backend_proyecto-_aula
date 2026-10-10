package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PosCheckoutRequest {

    /**
     * Correo del estudiante o cliente.
     * Si no se especifica y el pago es en efectivo/tarjeta, se asigna "pos.cliente@yirehstore.com".
     * Si el método es WALLET o MIXED, es obligatorio para debitar el saldo.
     */
    private String customerEmail;

    @NotEmpty(message = "Debe incluir al menos un producto para cobrar en caja")
    @Valid
    private List<PosItemRequest> items;

    /**
     * CASH, WALLET, CARD, MIXED
     */
    @NotNull(message = "El método de pago es requerido (CASH, WALLET, CARD, MIXED)")
    private String paymentMethod;

    /**
     * Monto en efectivo entregado por el cliente (para calcular cambio).
     */
    private BigDecimal cashReceived;

    /**
     * En caso de pago MIXTO: monto debitado de la billetera institucional del estudiante.
     */
    private BigDecimal walletAmount;

    private String notes;
}
