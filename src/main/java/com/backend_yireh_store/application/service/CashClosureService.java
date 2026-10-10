package com.backend_yireh_store.application.service;

import com.backend_yireh_store.domain.model.CashClosure;
import com.backend_yireh_store.domain.model.CashClosureStatus;
import com.backend_yireh_store.domain.model.User;
import com.backend_yireh_store.domain.port.in.CashClosureUseCase;
import com.backend_yireh_store.domain.port.out.CashClosureRepositoryPort;
import com.backend_yireh_store.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CashClosureService implements CashClosureUseCase {

    private final CashClosureRepositoryPort cashClosureRepository;
    private final UserRepositoryPort userRepository;

    public CashClosureService(CashClosureRepositoryPort cashClosureRepository, UserRepositoryPort userRepository) {
        this.cashClosureRepository = cashClosureRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CashClosure openClosure(String cashierEmail, BigDecimal openingAmount, String notes) {
        // Verificar si ya existe un turno abierto para este cajero
        Optional<CashClosure> existingTurn = cashClosureRepository.findFirstByCashierEmailAndStatus(
                cashierEmail, CashClosureStatus.OPEN);

        if (existingTurn.isPresent()) {
            throw new IllegalStateException("El cajero " + cashierEmail + " ya tiene un turno de caja abierto (ID: "
                    + existingTurn.get().getId() + "). Debe cerrarlo antes de aperturar uno nuevo.");
        }

        // Buscar el usuario cajero para obtener su ID
        User cashier = userRepository.findByEmail(cashierEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con correo: " + cashierEmail));

        CashClosure newClosure = CashClosure.openTurn(cashier.getId(), cashier.getEmail(), openingAmount, notes);
        return cashClosureRepository.save(newClosure);
    }

    @Override
    public CashClosure closeClosure(String cashierEmail, BigDecimal declaredCash, String notes) {
        // Buscar el turno abierto del cajero (o el turno abierto global si es administrador)
        CashClosure activeTurn = cashClosureRepository.findFirstByCashierEmailAndStatus(cashierEmail, CashClosureStatus.OPEN)
                .or(() -> cashClosureRepository.findFirstByStatus(CashClosureStatus.OPEN))
                .orElseThrow(() -> new IllegalStateException("No se encontró ningún turno de caja abierto para realizar el cierre."));

        activeTurn.closeTurn(declaredCash, notes);
        return cashClosureRepository.save(activeTurn);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CashClosure> getActiveClosure(String cashierEmail) {
        if (cashierEmail != null && !cashierEmail.isBlank()) {
            Optional<CashClosure> closure = cashClosureRepository.findFirstByCashierEmailAndStatus(
                    cashierEmail, CashClosureStatus.OPEN);
            if (closure.isPresent()) return closure;
        }
        return cashClosureRepository.findFirstByStatus(CashClosureStatus.OPEN);
    }

    @Override
    @Transactional(readOnly = true)
    public CashClosure getById(Long id) {
        return cashClosureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el turno de caja con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CashClosure> getAllClosures() {
        return cashClosureRepository.findAll();
    }
}
