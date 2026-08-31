package com.backend_yireh_store.domain.exception;

public class UserAlreadyExistsException extends DomainException {
    public UserAlreadyExistsException(String email) { super("Ya existe usuario con email: " + email); }
}
