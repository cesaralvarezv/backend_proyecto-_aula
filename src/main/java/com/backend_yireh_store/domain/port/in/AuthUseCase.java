package com.backend_yireh_store.domain.port.in;

import com.backend_yireh_store.domain.model.User;

/**
 * Input Port - casos de uso expuestos al exterior.
 */
public interface AuthUseCase {
    User register(String name, String phone, String email, String password);
    User login(String email, String password);
    User getByEmail(String email);
}
