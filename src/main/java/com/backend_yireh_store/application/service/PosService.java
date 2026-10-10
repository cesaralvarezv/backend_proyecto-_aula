package com.backend_yireh_store.application.service;

import com.backend_yireh_store.domain.model.*;
import com.backend_yireh_store.domain.port.in.PosUseCase;
import com.backend_yireh_store.domain.port.in.WalletUseCase;
import com.backend_yireh_store.domain.port.out.CashClosureRepositoryPort;
import com.backend_yireh_store.domain.port.out.OrderRepositoryPort;
import com.backend_yireh_store.domain.port.out.ProductRepositoryPort;
import com.backend_yireh_store.domain.port.out.UserRepositoryPort;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.PosCheckoutRequest;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.PosItemRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PosService implements PosUseCase {

    private final CashClosureRepositoryPort cashClosureRepository;
    private final ProductRepositoryPort productRepository;
    private final OrderRepositoryPort orderRepository;
    private final WalletUseCase walletUseCase;
    private final UserRepositoryPort userRepository;

    public PosService(CashClosureRepositoryPort cashClosureRepository,
                      ProductRepositoryPort productRepository,
                      OrderRepositoryPort orderRepository,
                      WalletUseCase walletUseCase,
                      UserRepositoryPort userRepository) {
        this.cashClosureRepository = cashClosureRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.walletUseCase = walletUseCase;
        this.userRepository = userRepository;
    }

    @Override
    public Order processPosCheckout(String cashierEmail, PosCheckoutRequest request) {
        // 1. Validar que exista un turno de caja abierto
        CashClosure activeClosure = cashClosureRepository.findFirstByCashierEmailAndStatus(cashierEmail, CashClosureStatus.OPEN)
                .or(() -> cashClosureRepository.findFirstByStatus(CashClosureStatus.OPEN))
                .orElseThrow(() -> new IllegalStateException("No hay un turno de caja abierto. Debe aperturar caja antes de realizar ventas."));

        User cashier = userRepository.findByEmail(cashierEmail)
                .orElseThrow(() -> new IllegalArgumentException("Cajero no encontrado: " + cashierEmail));

        // 2. Procesar productos, calcular total y reducir stock
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal orderTotal = BigDecimal.ZERO;

        for (PosItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + itemReq.getProductId()));

            if (!product.isActive()) {
                throw new IllegalStateException("El producto " + product.getName() + " se encuentra inactivo.");
            }

            // Reducir stock del producto
            product.reduceQuantity(itemReq.getQuantity());
            productRepository.save(product);

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            orderTotal = orderTotal.add(subtotal);

            orderItems.add(OrderItem.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .unitPrice(product.getPrice())
                    .quantity(itemReq.getQuantity())
                    .build());
        }

        // 3. Determinar cliente
        String method = request.getPaymentMethod().toUpperCase().trim();
        String customerEmail = (request.getCustomerEmail() != null && !request.getCustomerEmail().isBlank())
                ? request.getCustomerEmail().trim().toLowerCase()
                : "pos.cliente@yirehstore.com";

        BigDecimal cashReceived = request.getCashReceived() != null ? request.getCashReceived() : BigDecimal.ZERO;
        BigDecimal changeReturned = BigDecimal.ZERO;

        // 4. Gestión del pago y actualización del arqueo de caja
        switch (method) {
            case "WALLET" -> {
                if (customerEmail.equals("pos.cliente@yirehstore.com")) {
                    throw new IllegalArgumentException("Para pagar con saldo de billetera debe indicar el correo o escanear el QR del estudiante.");
                }
                walletUseCase.debit(customerEmail, orderTotal);
                activeClosure.addSale(orderTotal, "WALLET");
            }
            case "CARD", "DATAFONO" -> {
                activeClosure.addSale(orderTotal, "CARD");
            }
            case "MIXED" -> {
                if (customerEmail.equals("pos.cliente@yirehstore.com")) {
                    throw new IllegalArgumentException("Para pago mixto debe indicar el correo del estudiante para debitar la billetera.");
                }
                BigDecimal walletPart = request.getWalletAmount() != null ? request.getWalletAmount() : BigDecimal.ZERO;
                if (walletPart.compareTo(BigDecimal.ZERO) <= 0 || walletPart.compareTo(orderTotal) >= 0) {
                    throw new IllegalArgumentException("El monto a debitar de la billetera en pago mixto es inválido.");
                }
                BigDecimal cashPart = orderTotal.subtract(walletPart);

                if (cashReceived.compareTo(cashPart) < 0) {
                    throw new IllegalArgumentException("El efectivo recibido (" + cashReceived + ") es menor al remanente en efectivo requerido (" + cashPart + ").");
                }
                changeReturned = cashReceived.subtract(cashPart);

                walletUseCase.debit(customerEmail, walletPart);
                activeClosure.addSale(walletPart, "WALLET");
                activeClosure.addSale(cashPart, "CASH");
            }
            default -> { // CASH / EFECTIVO
                if (cashReceived.compareTo(orderTotal) < 0) {
                    throw new IllegalArgumentException("El efectivo recibido (" + cashReceived + ") es inferior al total de la compra (" + orderTotal + ").");
                }
                changeReturned = cashReceived.subtract(orderTotal);
                activeClosure.addSale(orderTotal, "CASH");
            }
        }

        // Guardar el estado actualizado de la caja
        cashClosureRepository.save(activeClosure);

        // 5. Persistir la orden como venta directa de mostrador entregada inmediatamente
        String orderNumber = orderRepository.generateOrderNumber();
        LocalDateTime now = LocalDateTime.now();

        Order posOrder = Order.builder()
                .orderNumber(orderNumber)
                .userEmail(customerEmail)
                .items(orderItems)
                .total(orderTotal)
                .status(OrderStatus.DELIVERED) // Venta física entregada en mostrador
                .paymentMethod(method)
                .deliveryLocation("Caja Mostrador Principal")
                .notes(request.getNotes() != null ? request.getNotes().trim() : "Venta POS Mostrador")
                .orderType("POS_COUNTER")
                .cashierId(cashier.getId())
                .closureId(activeClosure.getId())
                .cashReceived(cashReceived)
                .changeReturned(changeReturned)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return orderRepository.save(posOrder);
    }
}
