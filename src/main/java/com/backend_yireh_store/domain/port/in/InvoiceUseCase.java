package com.backend_yireh_store.domain.port.in;

public interface InvoiceUseCase {

    byte[] generateInvoicePdf(Long orderId);

    void sendInvoiceEmail(Long orderId, String recipientEmail);
}
