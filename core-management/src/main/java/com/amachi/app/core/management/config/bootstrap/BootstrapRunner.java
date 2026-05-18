package com.amachi.app.core.management.config.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootstrapRunner {

    private final BootstrapService bootstrapService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    @Order(1)
    public void onApplicationReady() {
        log.info("🔹 Running Bootstrap Process..");
        try {
            bootstrapService.runBootstrap();
            log.info("✅ Bootstrap Process completed successfully.");
        } catch (Exception ex) {
            log.error("❌ Bootstrap Process failed!", ex);
            throw ex; // Importante para fallar rápido si hay error
        }
    }
}
