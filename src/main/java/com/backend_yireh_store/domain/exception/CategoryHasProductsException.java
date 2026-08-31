package com.backend_yireh_store.domain.exception;

public class CategoryHasProductsException extends DomainException {
    public CategoryHasProductsException(String categoryName) {
        super("No se puede eliminar la categoría '" + categoryName + "' porque tiene productos asociados. Primero debes reasignar o eliminar los productos.");
    }
}