package com.backend_yireh_store.application.service;

import com.backend_yireh_store.domain.model.Recharge;
import com.backend_yireh_store.domain.model.User;
import com.backend_yireh_store.domain.port.in.WalletUseCase;
import com.backend_yireh_store.domain.port.out.RechargeRepositoryPort;
import com.backend_yireh_store.domain.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class WalletService implements WalletUseCase {

    private final UserRepositoryPort userRepository;
    private final RechargeRepositoryPort rechargeRepository;

    public WalletService(UserRepositoryPort userRepository,
                         RechargeRepositoryPort rechargeRepository) {
        this.userRepository = userRepository;
        this.rechargeRepository = rechargeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .map(User::getBalance)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userEmail));
    }

    @Override
    @Transactional
    public Recharge recharge(String userEmail, BigDecimal amount, String paymentMethod, String referenceCode) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto de recarga debe ser mayor a 0");
        }
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userEmail));

        BigDecimal newBalance = user.getBalance().add(amount);
        userRepository.updateBalance(userEmail, newBalance);

        String ref = (referenceCode != null && !referenceCode.isBlank())
                ? referenceCode
                : "REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Recharge recharge = Recharge.builder()
                .userId(userEmail)
                .amount(amount)
                .paymentMethod(paymentMethod != null ? paymentMethod.toUpperCase() : "CASH")
                .status("COMPLETED")
                .referenceCode(ref)
                .createdAt(LocalDateTime.now())
                .build();
        return rechargeRepository.save(recharge);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Recharge> getTransactions(String userEmail) {
        return rechargeRepository.findByUserId(userEmail);
    }

    @Override
    @Transactional
    public void debit(String userEmail, BigDecimal amount) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userEmail));
        if (user.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Saldo insuficiente. Disponible: " + user.getBalance());
        }
        userRepository.updateBalance(userEmail, user.getBalance().subtract(amount));
    }
}
