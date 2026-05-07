package com.amachi.app.vitalia.medicalcore.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "UserProfile", description = "Perfil curricular extendido del profesional de salud")
public class UserProfileDto {

    @Schema(description = "Identificador único del perfil", example = "301", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Biografía o resumen profesional", example = "15 años en cuidados intensivos neonatales.")
    private String biography;

    @Schema(description = "Formación académica del profesional")
    @Builder.Default
    private List<EducationDto> educationList = new ArrayList<>();

    @Schema(description = "Trayectoria laboral previa")
    @Builder.Default
    private List<ExperienceDto> experienceList = new ArrayList<>();

    @Schema(description = "Cursos y capacitaciones realizadas")
    @Builder.Default
    private List<CourseDto> courseList = new ArrayList<>();

    @Schema(description = "Participaciones en congresos y eventos científicos")
    @Builder.Default
    private List<ConferenceDto> conferenceList = new ArrayList<>();
}
