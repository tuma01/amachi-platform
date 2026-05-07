package com.amachi.app.vitalia.medicalcore.appointment.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.core.common.enums.ReminderStatus;
import com.amachi.app.vitalia.medicalcore.common.enums.ReminderChannel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para Recordatorios de Cita")
public class AppointmentReminderSearchDto extends BaseSearchDto {

    @Schema(description = "ID de la cita", example = "9001")
    private Long appointmentId;

    @Schema(description = "Canal de envío", example = "EMAIL")
    private ReminderChannel channel;

    @Schema(description = "Estado del recordatorio", example = "PENDING")
    private ReminderStatus status;
}
