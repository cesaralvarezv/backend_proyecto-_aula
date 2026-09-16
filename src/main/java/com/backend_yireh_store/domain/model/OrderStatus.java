package com.backend_yireh_store.domain.model;

public enum OrderStatus {
    PENDING,     // Recién creado — esperando confirmación
    CONFIRMED,   // Confirmado por la tienda
    PREPARING,   // En preparación
    READY,       // Listo para recoger
    DELIVERED,   // Entregado al cliente
    CANCELLED    // Cancelado
}
