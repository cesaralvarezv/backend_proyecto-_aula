package com.backend_yireh_store.infrastructure.config;

import com.backend_yireh_store.domain.exception.CategoryAlreadyExistsException;
import com.backend_yireh_store.domain.exception.CategoryHasProductsException;
import com.backend_yireh_store.domain.exception.CategoryNotFoundException;
import com.backend_yireh_store.domain.exception.InvalidCredentialsException;
import com.backend_yireh_store.domain.exception.ProductAlreadyExistsException;
import com.backend_yireh_store.domain.exception.ProductNotFoundException;
import com.backend_yireh_store.domain.exception.UserAlreadyExistsException;
import com.backend_yireh_store.domain.port.in.LogUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final LogUseCase logUseCase;

    public GlobalExceptionHandler(LogUseCase logUseCase) {
        this.logUseCase = logUseCase;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleExists(UserAlreadyExistsException ex) {
        saveLog("WARN", "UserAlreadyExists", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalid(InvalidCredentialsException ex) {
        saveLog("WARN", "InvalidCredentials", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        saveLog("WARN", "BadRequest", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .findFirst().orElse("Validacion fallida");
        saveLog("WARN", "Validation", msg, ex);
        return ResponseEntity.badRequest().body(Map.of("error", msg));
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleCategoryExists(CategoryAlreadyExistsException ex) {
        saveLog("WARN", "CategoryAlreadyExists", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCategoryNotFound(CategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(CategoryHasProductsException.class)
    public ResponseEntity<Map<String, String>> handleCategoryHasProducts(CategoryHasProductsException ex) {
        saveLog("WARN", "CategoryHasProducts", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleProductExists(ProductAlreadyExistsException ex) {
        saveLog("WARN", "ProductAlreadyExists", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex) {
        saveLog("WARN", "AccessDenied", "No autorizado: " + ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "No tienes permiso para realizar esta accion"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
        saveLog("ERROR", "UnhandledException", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
    }

    private void saveLog(String level, String tag, String message, Exception ex) {
        try {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            logUseCase.registerBackend(level, tag, message, sw.toString());
        } catch (Exception e) {
            log.error("No se pudo guardar log: {}", e.getMessage());
        }
    }
}
