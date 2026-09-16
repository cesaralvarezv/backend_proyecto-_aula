package com.backend_yireh_store.domain.port.in;

import com.backend_yireh_store.domain.model.User;
import java.util.List;

public interface UserUseCase {
    User createAdmin(String name, String phone, String email, String password, String role);
    List<User> getAll();
    User getById(Long id);
    /** Obtiene perfil del usuario por email. */
    User getProfile(String email);
    /** Actualiza nombre y teléfono del usuario autenticado. */
    User updateProfile(String email, String name, String phone);
}

