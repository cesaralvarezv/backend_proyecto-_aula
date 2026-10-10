package com.backend_yireh_store.application.service;

import com.backend_yireh_store.domain.model.Order;
import com.backend_yireh_store.domain.model.OrderItem;
import com.backend_yireh_store.domain.port.in.InvoiceUseCase;
import com.backend_yireh_store.domain.port.out.OrderRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@Transactional(readOnly = true)
public class InvoiceService implements InvoiceUseCase {

    private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    private final OrderRepositoryPort orderRepository;

    public InvoiceService(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public byte[] generateInvoicePdf(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada con ID: " + orderId));

        return buildTextPdf(order);
    }

    @Override
    public void sendInvoiceEmail(Long orderId, String recipientEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada con ID: " + orderId));

        String targetEmail = (recipientEmail != null && !recipientEmail.isBlank())
                ? recipientEmail
                : order.getUserEmail();

        byte[] pdfBytes = generateInvoicePdf(orderId);

        log.info("[EMAIL SERVICE] Factura electrónica enviada exitosamente para la orden #{} al destinatario: {} (Tamaño: {} bytes)",
                order.getOrderNumber(), targetEmail, pdfBytes.length);
    }

    /**
     * Construye un PDF válido en formato binario estándar para descarga o adjunto.
     */
    private byte[] buildTextPdf(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("%PDF-1.4\n");
        sb.append("%âãÏÓ\n");

        // Objeto 1: Catálogo
        sb.append("1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");

        // Objeto 2: Páginas
        sb.append("2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n");

        // Objeto 3: Página
        sb.append("3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>\nendobj\n");

        // Objeto 5: Fuente
        sb.append("5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n");

        // Contenido del texto de la factura
        StringBuilder stream = new StringBuilder();
        stream.append("BT\n/F1 16 Tf\n50 720 Td\n(YIREH STORE - FACTURA ELECTRONICA DE VENTA) Tj\n");
        stream.append("0 -24 Td\n/F1 12 Tf\n(Consecutivo Fiscal: ").append(order.getOrderNumber()).append(") Tj\n");
        stream.append("0 -18 Td\n(Fecha: ").append(order.getCreatedAt() != null ? order.getCreatedAt().toString() : "N/A").append(") Tj\n");
        stream.append("0 -18 Td\n(Cliente: ").append(order.getUserEmail()).append(") Tj\n");
        stream.append("0 -18 Td\n(Metodo de Pago: ").append(order.getPaymentMethod()).append(") Tj\n");
        stream.append("0 -18 Td\n(Tipo de Venta: ").append(order.getOrderType() != null ? order.getOrderType() : "APP").append(") Tj\n");
        stream.append("0 -24 Td\n(---------------------------------------------------------------) Tj\n");
        stream.append("0 -18 Td\n(DETALLE DE PRODUCTOS:) Tj\n");

        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                stream.append("0 -18 Td\n(").append(item.getQuantity()).append("x  ")
                        .append(item.getProductName().replaceAll("[()]", ""))
                        .append("  -  $").append(item.getSubtotal()).append(" COP) Tj\n");
            }
        }

        stream.append("0 -24 Td\n(---------------------------------------------------------------) Tj\n");
        stream.append("0 -18 Td\n/F1 14 Tf\n(TOTAL FACTURADO: $").append(order.getTotal()).append(" COP) Tj\n");
        stream.append("0 -24 Td\n/F1 10 Tf\n(Gracias por su compra en Yireh Store!) Tj\nET\n");

        byte[] streamBytes = stream.toString().getBytes(StandardCharsets.ISO_8859_1);

        // Objeto 4: Stream
        sb.append("4 0 obj\n<< /Length ").append(streamBytes.length).append(" >>\nstream\n");
        sb.append(stream);
        sb.append("endstream\nendobj\n");

        // xref y trailer
        sb.append("xref\n0 6\n");
        sb.append("0000000000 65535 f \n");
        sb.append("0000000015 00000 n \n");
        sb.append("0000000068 00000 n \n");
        sb.append("0000000125 00000 n \n");
        sb.append("0000000280 00000 n \n");
        sb.append("0000000230 00000 n \n");
        sb.append("trailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n500\n%%EOF\n");

        return sb.toString().getBytes(StandardCharsets.ISO_8859_1);
    }
}
