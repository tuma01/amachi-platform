package com.amachi.app.vitalia.medicalcore.appointment.dto;

import com.amachi.app.core.common.enums.AppointmentStatus;
import com.amachi.app.vitalia.medicalcore.common.enums.AppointmentSource;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.OffsetDateTime;

/**
 * Esquema profesional para la gestion de agendas y citas medicas.
 * Estandar de Oro de Vitalia.
 */
@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Appointment", description = "Control operacional del workflow de citas medicas nivel SaaS Enterprise (Agenda/Atención)")
public class AppointmentDto {

    @Schema(description = "Identificador unico de la cita", example = "9001")
    private Long id;

    // --- Identidad y Vinculación ---
    @NotNull(message = "Paciente {err.mandatory}")
    @Schema(description = "ID del paciente solicitado", example = "5001")
    private Long patientId;

    @Schema(description = "Nombre completo del paciente (Solo lectura)", example = "JUAN CARLOS PEREZ GARCIA")
    private String patientFullName;

    @NotNull(message = "Medico Solicitado {err.mandatory}")
    @Schema(description = "ID del facultativo agendado", example = "2001")
    private Long doctorId;

    @Schema(description = "Nombre del medico asignado (Solo lectura)", example = "DR. MARCOS SOLIZ")
    private String doctorFullName;

    // --- Agenda y Tiempos SaaS ---
    @NotNull(message = "Hora Inicio {err.mandatory}")
    @Schema(description = "Fecha y hora formal de inicio del bloque", example = "2026-03-30T10:30:00Z")
    private OffsetDateTime startTime;

    @NotNull(message = "Hora Conclusion {err.mandatory}")
    @Schema(description = "Fecha y hora estimada de finalizacion", example = "2026-03-30T11:00:00Z")
    private OffsetDateTime endTime;

    // --- Infraestructura y Ubicación ---
    @Schema(description = "ID de la unidad hospitalaria (Pabellon/Servicio)", example = "101")
    private Long unitId;

    @Schema(description = "Nombre de la unidad (Solo lectura)", example = "PEDIATRIA - ALA SUR")
    private String unitName;

    @Schema(description = "ID del consultorio o box fisico", example = "202")
    private Long roomId;

    @Schema(description = "Referencia visual del consultorio asignado", example = "PLANTA BAJA - BOX 01")
    private String roomNumber;

    // --- Clasificación y Workflow ---
    @Schema(description = "Tipo de cita medica (CONSULTATION, FOLLOW_UP, etc.)", example = "CONSULTATION")
    private String type;

    @Schema(description = "Nivel de prioridad (NORMAL, URGENT, VIP)", example = "NORMAL")
    private String priority;

    @Schema(description = "Estado administrativo del workflow", example = "PENDING")
    private AppointmentStatus status;

    @Schema(description = "Canal de origen por el cual se agendo la cita (ONLINE, PHONE, WALK_IN, etc.)", example = "ONLINE")
    private AppointmentSource source;

    @Schema(description = "Indica si el paciente no se presento a la cita (No-Show)", example = "false")
    private Boolean noShow;

    @Schema(description = "ID de la visita clinica vinculada. Solo lectura: se asigna automaticamente al registrar el arribo.", example = "12001")
    private Long visitId;

    @Schema(description = "Motivo de la consulta o descripcion de sintomas", example = "CONTROL POST-QUIRURGICO")
    private String reason;

    // --- Métricas de Atención (Lifecycle) ---
    @Schema(description = "Timestamp real de llegada al hospital", example = "2026-03-30T10:25:00Z")
    private OffsetDateTime checkedInAt;

    @Schema(description = "Timestamp real de finalizacion del acto medico", example = "2026-03-30T10:55:00Z")
    private OffsetDateTime completedAt;

    // --- Control de Concurrencia Multi-Operador (Solo lectura) ---

    @Schema(
        description = "Timestamp hasta el cual la cita esta bloqueada por un operador activo. Solo lectura.",
        accessMode = Schema.AccessMode.READ_ONLY,
        example = "2026-03-30T10:35:00Z"
    )
    private OffsetDateTime lockedUntil;

    @Schema(
        description = "Identificador del operador que mantiene el bloqueo activo. Solo lectura.",
        accessMode = Schema.AccessMode.READ_ONLY,
        example = "jmolina@vitalia.com"
    )
    private String lockedBy;

    // --- Desestimiento y Cancelaciones ---
    @Schema(description = "Razón formal por la que se canceló el agendamiento", example = "Paciente solicita reprogramación por viaje")
    private String cancelReason;

    @Schema(description = "Fecha exacta de la cancelación", example = "2026-03-29T18:00:00Z")
    private OffsetDateTime cancelledAt;
}
