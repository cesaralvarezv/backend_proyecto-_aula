package com.backend_yireh_store.domain.port.in;

import com.backend_yireh_store.domain.model.Recharge;
import java.math.BigDecimal;
import java.util.List;

public interface WalletUseCase {
    /** Obtiene el saldo actual del usuario (por email). */
    BigDecimal getBalance(String userEmail);

    /** Aplica una recarga al saldo del usuario. Devuelve la recarga registrada. */
    Recharge recharge(String userEmail, BigDecimal amount, String paymentMethod, String referenceCode);

    /** Historial de recargas/movimientos del usuario. */
    List<Recharge> getTransactions(String userEmail);

    /** Debita saldo — usado internamente por el servicio de checkout. */
    void debit(String userEmail, BigDecimal amount);
}
