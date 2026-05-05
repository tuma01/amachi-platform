package com.amachi.app.core.domain.dto;

import com.amachi.app.core.common.enums.RelationStatus;
import com.amachi.app.core.common.enums.RoleContext;
import com.amachi.app.core.domain.tenant.dto.TenantDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "PersonDto", description = "Información detallada de una persona asociada a un tenant específico")
public class PersonTenantDto implements Serializable {

    @Schema(description = "ID único de la relación persona-tenant", example = "1")
    private Long id;

    @Schema(description = "Información de la persona asociada", example = "Juan Pérez")
    private PersonDto person;

    @Schema(description = "Información del tenant asociado", example = "Hospital ABC")
    private TenantDto tenant;

    @Schema(description = "ID de salud nacional específico para esta relación persona-tenant", example = "NHS-999")
    private String nationalHealthId;

    @Schema(description = "Contexto del rol que la persona desempeña en el tenant", example = "DOCTOR, PATIENT, etc.")
    private RoleContext roleContext;

    @Schema(description = "Fecha en que la persona se registró en el tenant", example = "2024-01-01T12:00:00")
    private LocalDateTime dateRegistered;

    @Schema(description = "Fecha en que la persona se desregistró del tenant (si aplica)", example = "2024-06-01T12:00:00")
    private LocalDateTime  dateUnregistered;

    @Schema(description = "Estado de la relación entre la persona y el tenant", example = "ACTIVO, INACTIVO, SUSPENDIDO, etc.")
    private RelationStatus relationStatus;
}
