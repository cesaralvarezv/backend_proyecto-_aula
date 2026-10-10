package com.backend_yireh_store.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo de Dominio para el Arqueo y Cierre de Caja (Z-Report).
 * Contiene reglas de negocio puras, sin dependencias de infraestructura ni frameworks.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashClosure {

    private Long id;
    private Long cashierId;
    private String cashierEmail;
    private BigDecimal openingAmount;
    private LocalDateTime openingTime;
    private LocalDateTime closingTime;
    
    @Builder.Default
    private BigDecimal cashSalesSystem = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal virtualWalletSales = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal cardSales = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal totalSalesSystem = BigDecimal.ZERO;

    private BigDecimal declaredCash;
    private BigDecimal discrepancy;

    @Builder.Default
    private CashClosureStatus status = CashClosureStatus.OPEN;

    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Fábrica de dominio para abrir un nuevo turno de caja.
     */
    public static CashClosure openTurn(Long cashierId, String cashierEmail, BigDecimal openingAmount, String notes) {
        if (cashierId == null || cashierEmail == null || cashierEmail.isBlank()) {
            throw new IllegalArgumentException("El cajero es obligatorio para aperturar caja.");
        }
        if (openingAmount == null || openingAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto base inicial de caja debe ser mayor o igual a cero.");
        }

        LocalDateTime now = LocalDateTime.now();
        return CashClosure.builder()
                .cashierId(cashierId)
                .cashierEmail(cashierEmail.trim().toLowerCase())
                .openingAmount(openingAmount)
                .openingTime(now)
                .cashSalesSystem(BigDecimal.ZERO)
                .virtualWalletSales(BigDecimal.ZERO)
                .cardSales(BigDecimal.ZERO)
                .totalSalesSystem(BigDecimal.ZERO)
                .status(CashClosureStatus.OPEN)
                .notes(notes)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    /**
     * Cierra el turno de caja, calculando el descuadre y bloqueando el turno.
     * Descuadre = Efectivo Declarado - (Monto Base Inicial + Ventas en Efectivo registradas por Sistema).
     */
    public void closeTurn(BigDecimal declaredCash, String closingNotes) {
        if (this.status == CashClosureStatus.CLOSED) {
            throw new IllegalStateException("El turno de caja ya se encuentra cerrado.");
        }
        if (declaredCash == null || declaredCash.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El monto de efectivo físico declarado no puede ser nulo ni negativo.");
        }

        this.declaredCash = declaredCash;
        this.closingTime = LocalDateTime.now();
        this.updatedAt = this.closingTime;

        BigDecimal expectedPhysicalCash = this.openingAmount.add(this.cashSalesSystem != null ? this.cashSalesSystem : BigDecimal.ZERO);
        this.discrepancy = this.declaredCash.subtract(expectedPhysicalCash);

        if (closingNotes != null && !closingNotes.isBlank()) {
            this.notes = (this.notes != null && !this.notes.isBlank())
                    ? this.notes + " | Cierre: " + closingNotes.trim()
                    : closingNotes.trim();
        }

        this.status = CashClosureStatus.CLOSED;
    }

    /**
     * Acumula una venta al turno abierto según su método de pago.
     */
    public void addSale(BigDecimal amount, String paymentMethod) {
        if (this.status != CashClosureStatus.OPEN) {
            throw new IllegalStateException("No se pueden registrar ventas en una caja cerrada.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        if (this.cashSalesSystem == null) this.cashSalesSystem = BigDecimal.ZERO;
        if (this.virtualWalletSales == null) this.virtualWalletSales = BigDecimal.ZERO;
        if (this.cardSales == null) this.cardSales = BigDecimal.ZERO;
        if (this.totalSalesSystem == null) this.totalSalesSystem = BigDecimal.ZERO;

        String method = (paymentMethod != null) ? paymentMethod.toUpperCase().trim() : "CASH";

        switch (method) {
            case "WALLET" -> this.virtualWalletSales = this.virtualWalletSales.add(amount);
            case "CARD", "DATAFONO" -> this.cardSales = this.cardSales.add(amount);
            default -> this.cashSalesSystem = this.cashSalesSystem.add(amount); // CASH, EFECTIVO u otros
        }

        this.totalSalesSystem = this.totalSalesSystem.add(amount);
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isOpen() {
        return this.status == CashClosureStatus.OPEN;
    }
}
