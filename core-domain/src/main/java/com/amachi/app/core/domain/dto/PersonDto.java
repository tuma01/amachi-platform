package com.amachi.app.core.domain.dto;

import com.amachi.app.core.common.enums.RoleContext;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Set;

/**
 * Data Transfer Object for Person entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Schema(name = "PersonDto", description = "Información detallada de una persona")
public class PersonDto implements Serializable {

        private Long id;

        @Schema(description = "Nombre principal", example = "Juan")
        private String firstName;

        @Schema(description = "Segundo nombre", example = "Carlos")
        private String middleName;

        @Schema(description = "Apellido paterno", example = "Pérez")
        private String lastName;

        @Schema(description = "Apellido materno", example = "García")
        private String secondLastName;

        @Schema(description = "Correo electrónico personal", example = "juan.perez@example.com")
        private String email;

        @Schema(description = "Número de identificación nacional (DNI, Pasaporte)", example = "12345678")
        private String nationalId;

        @Schema(description = "ID de salud nacional", example = "NHS-999")
        private String nationalHealthId;

        @Schema(description = "Tipo de documento de identificación", example = "DNI")
        private String documentType;

        @Schema(description = "Fecha de nacimiento", example = "1990-01-01")
        private java.time.LocalDate birthDate;

        @Schema(description = "Género de la persona", example = "MASCULINO")
        private String gender;

        @Schema(description = "Estado civil", example = "SOLTERO/A")
        private String maritalStatus;

        @Schema(description = "Número de teléfono fijo", example = "22334455")
        private String phoneNumber;

        @Schema(description = "Número de teléfono móvil", example = "66778899")
        private String mobileNumber;

        @Schema(description = "Dirección de residencia")
        private com.amachi.app.core.geography.address.dto.AddressDto address;

        @Schema(description = "Contextos de rol activos para esta persona")
        private Set<RoleContext> activeRoleContexts;

        public String getFullName() {
                StringBuilder sb = new StringBuilder();
                if (firstName != null) sb.append(firstName);
                if (middleName != null && !middleName.isBlank()) sb.append(" ").append(middleName);
                if (lastName != null) sb.append(" ").append(lastName);
                if (secondLastName != null && !secondLastName.isBlank()) sb.append(" ").append(secondLastName);
                return sb.toString().trim();
        }
}
