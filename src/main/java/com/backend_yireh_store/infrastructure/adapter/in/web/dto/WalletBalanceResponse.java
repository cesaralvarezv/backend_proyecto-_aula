package com.backend_yireh_store.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;

public record WalletBalanceResponse(
        String userId,
        BigDecimal balance
) {}
