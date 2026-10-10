package com.backend_yireh_store.infrastructure.adapter.in.web;

import com.backend_yireh_store.domain.port.in.InvoiceUseCase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/emails")
@CrossOrigin(origins = "*")
public class InvoiceController {

    private final InvoiceUseCase invoiceUseCase;

    public InvoiceController(InvoiceUseCase invoiceUseCase) {
        this.invoiceUseCase = invoiceUseCase;
    }

    /**
     * POST /api/emails/send-invoice/{orderId}
     * Disparo manual o reintento de envío de factura PDF al correo del cliente.
     */
    @PostMapping("/send-invoice/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> sendInvoice(
            @PathVariable Long orderId,
            @RequestParam(required = false) String email) {

        invoiceUseCase.sendInvoiceEmail(orderId, email);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Factura digital enviada exitosamente para la orden ID: " + orderId
        ));
    }

    /**
     * GET /api/emails/invoice/{orderId}/pdf
     * Descarga del documento PDF generado en memoria.
     */
    @GetMapping("/invoice/{orderId}/pdf")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Long orderId) {
        byte[] pdfBytes = invoiceUseCase.generateInvoicePdf(orderId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"factura_orden_" + orderId + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
