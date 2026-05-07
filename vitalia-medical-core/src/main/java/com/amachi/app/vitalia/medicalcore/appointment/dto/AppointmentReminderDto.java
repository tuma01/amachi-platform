package com.amachi.app.vitalia.medicalcore.appointment.dto;

import com.amachi.app.core.common.enums.ReminderStatus;
import com.amachi.app.vitalia.medicalcore.common.enums.ReminderChannel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "AppointmentReminder", description = "Recordatorio omnicanal de cita médica (SMS, Email, Push)")
public class AppointmentReminderDto {

    @Schema(description = "Identificador único", example = "9501", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Cita {err.mandatory}")
    @Schema(description = "ID de la cita a la que pertenece el recordatorio", example = "9001")
    private Long appointmentId;

    @NotNull(message = "Canal {err.mandatory}")
    @Schema(description = "Canal de envío del recordatorio", example = "EMAIL")
    private ReminderChannel channel;

    @NotNull(message = "Estado {err.mandatory}")
    @Schema(description = "Estado del ciclo de vida del recordatorio", example = "PENDING")
    private ReminderStatus status;

    @NotNull(message = "Fecha programada {err.mandatory}")
    @Schema(description = "Fecha y hora programada de envío", example = "2026-05-07T08:00:00")
    private LocalDateTime scheduledDate;

    @Schema(description = "Fecha y hora real de envío", example = "2026-05-07T08:00:05", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime sentDate;

    @Schema(description = "Fecha y hora de lectura por el destinatario", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime readDate;

    @Schema(description = "Número de reintentos realizados", example = "0", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer retryCount;

    @Schema(description = "Destino del envío (email, número de teléfono)", example = "paciente@email.com")
    private String target;

    @Schema(description = "ID del mensaje en el gateway externo", accessMode = Schema.AccessMode.READ_ONLY)
    private String externalMessageId;

    @Schema(description = "Respuesta raw del gateway", accessMode = Schema.AccessMode.READ_ONLY)
    private String gatewayResponse;
}
