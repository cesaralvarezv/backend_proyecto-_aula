package com.backend_yireh_store.domain.port.out;

import com.backend_yireh_store.domain.model.Recharge;
import java.util.List;

public interface RechargeRepositoryPort {
    Recharge save(Recharge recharge);
    List<Recharge> findByUserId(String userId);
}
