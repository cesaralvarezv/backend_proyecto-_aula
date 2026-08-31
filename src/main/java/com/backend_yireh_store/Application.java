package com.backend_yireh_store;

import com.backend_yireh_store.domain.port.out.CategoryRepositoryPort;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        // Carga .env ANTES de que Spring resuelva ${DB_*} en application.properties
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .ignoreIfMissing()
                    .ignoreIfMalformed()
                    .load();
            dotenv.entries().forEach(e -> {
                if (System.getProperty(e.getKey()) == null) {
                    System.setProperty(e.getKey(), e.getValue());
                }
            });
            System.out.println("[Dotenv] Cargado .env con " + dotenv.entries().size() + " vars, DB_USERNAME=" + System.getProperty("DB_USERNAME"));
        } catch (Exception e) {
            System.out.println("[Dotenv] No se pudo cargar .env: " + e.getMessage());
        }
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner fixLegacyCategories(CategoryRepositoryPort categoryRepository) {
        return args -> {
            // Corrige categorías legacy que tengan active=false (creadas antes del campo active)
            // Asumimos que categorías sin active explícito deben ser activas
            categoryRepository.findAll().stream()
                    .filter(c -> !c.isActive())
                    .forEach(c -> {
                        c.activate();
                        categoryRepository.save(c);
                        System.out.println("[Fix] Categoría activada: " + c.getName() + " (ID: " + c.getId() + ")");
                    });
        };
    }
}