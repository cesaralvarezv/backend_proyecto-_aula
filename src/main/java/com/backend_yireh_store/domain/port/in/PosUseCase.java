package com.backend_yireh_store.domain.port.in;

import com.backend_yireh_store.domain.model.Order;
import com.backend_yireh_store.infrastructure.adapter.in.web.dto.PosCheckoutRequest;

public interface PosUseCase {

    Order processPosCheckout(String cashierEmail, PosCheckoutRequest request);
}
