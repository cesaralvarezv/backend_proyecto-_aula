package com.backend_yireh_store.domain.port.out;

/**
 * ISP: interfaz pequeña solo para encoding/verificación.
 */
public interface PasswordEncoderPort {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
