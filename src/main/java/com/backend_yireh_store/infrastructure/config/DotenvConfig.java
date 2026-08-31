package com.backend_yireh_store.infrastructure.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

/**
 * Carga .env antes que Spring lea application.properties.
 * Permite usar ${DB_URL} etc. definidos en .env (SRP, OCP).
 */
@Configuration
public class DotenvConfig {

    @PostConstruct
    public void loadEnv() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .ignoreIfMissing()
                    .ignoreIfMalformed()
                    .load();
            dotenv.entries().forEach(entry -> {
                // Solo setea si no existe ya como System/env
                if (System.getProperty(entry.getKey()) == null && System.getenv(entry.getKey()) == null) {
                    System.setProperty(entry.getKey(), entry.getValue());
                }
            });
            System.out.println("[Dotenv] .env cargado con " + dotenv.entries().size() + " variables");
        } catch (Exception e) {
            System.out.println("[Dotenv] No se encontró .env, usando variables de entorno del sistema");
        }
    }
}
