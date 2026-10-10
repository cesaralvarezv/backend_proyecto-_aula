package com.backend_yireh_store.domain.port.in;

import com.backend_yireh_store.domain.model.CashClosure;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CashClosureUseCase {

    CashClosure openClosure(String cashierEmail, BigDecimal openingAmount, String notes);

    CashClosure closeClosure(String cashierEmail, BigDecimal declaredCash, String notes);

    Optional<CashClosure> getActiveClosure(String cashierEmail);

    CashClosure getById(Long id);

    List<CashClosure> getAllClosures();
}
