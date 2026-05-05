package com.amachi.app.vitalia.medicalcore.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ≡ƒö╣ Ciclo de vida cl├¡nico del acto m├⌐dico (FHIR Encounter Status).
 * Diferencia la log├¡stica (Appointment) de la atenci├│n cl├¡nica real.
 */
@Schema(description = "Estado cl├¡nico del encuentro m├⌐dico")
public enum EncounterStatus {
    PLANNED,       // Creado pero no iniciado (pre-admisi├│n)
    IN_PROGRESS,   // Paciente siendo atendido por el profesional
    ON_HOLD,       // Interrupci├│n temporal (ej: espera de resultados de laboratorio)
    COMPLETED,     // Acto m├⌐dico finalizado correctamente
    CANCELLED,     // Cancelado antes de finalizar
    UNKNOWN        // Estado desconocido (Interoperabilidad external)
}
