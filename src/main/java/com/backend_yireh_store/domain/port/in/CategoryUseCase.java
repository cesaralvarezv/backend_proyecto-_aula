package com.backend_yireh_store.domain.port.in;

import com.backend_yireh_store.domain.model.Category;
import java.util.List;

public interface CategoryUseCase {
    Category create(String name, String description, String icon, String location, int order);
    Category getById(Long id);
    List<Category> getAll();
    List<Category> getActive();
    List<Category> getActiveByLocation(String location);
    Category update(Long id, String name, String description, String icon, String location, Integer order);
    Category deactivate(Long id);
    Category activate(Long id);
    void delete(Long id);
}
