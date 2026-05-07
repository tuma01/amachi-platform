package com.amachi.app.vitalia.medicalcore.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Estado operativo de limpieza de habitaciones y camas hospitalarias.
 * Controla la disponibilidad sanitaria del recurso físico.
 */
@Schema(description = "Estado de limpieza del recurso físico hospitalario")
public enum CleaningStatus {
    CLEAN,          // Limpio y disponible para ocupar
    DIRTY,          // Requiere limpieza antes de uso
    IN_PROGRESS,    // Proceso de limpieza en curso
    MAINTENANCE,    // En mantenimiento preventivo o correctivo
    DISINFECTING    // En proceso de desinfección profunda
}
