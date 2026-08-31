package com.backend_yireh_store.domain.exception;

public class InvalidCredentialsException extends DomainException {
    public InvalidCredentialsException() { super("Credenciales inválidas"); }
}
