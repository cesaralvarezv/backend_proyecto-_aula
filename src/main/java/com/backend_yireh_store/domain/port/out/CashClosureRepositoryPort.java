package com.backend_yireh_store.domain.port.out;

import com.backend_yireh_store.domain.model.CashClosure;
import com.backend_yireh_store.domain.model.CashClosureStatus;

import java.util.List;
import java.util.Optional;

public interface CashClosureRepositoryPort {

    CashClosure save(CashClosure cashClosure);

    Optional<CashClosure> findById(Long id);

    Optional<CashClosure> findFirstByStatus(CashClosureStatus status);

    Optional<CashClosure> findFirstByCashierEmailAndStatus(String cashierEmail, CashClosureStatus status);

    List<CashClosure> findAll();
}
