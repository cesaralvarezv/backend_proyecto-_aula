package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

public record CheckoutRequest(
        String paymentMethod,    // WALLET, CASH, NEQUI, DAVIPLATA
        String deliveryLocation, // Ej: "Piso 3 - Local 302"
        String notes             // Comentarios adicionales (opcional)
) {}
