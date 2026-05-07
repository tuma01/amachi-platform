package com.amachi.app.vitalia.medicalcore.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Valoración subjetiva de la calidad del sueño del paciente.
 * Utilizada en la evaluación de hábitos fisiológicos del historial clínico.
 */
@Schema(description = "Calidad del sueño autoreportada por el paciente")
public enum SleepQuality {
    EXCELLENT,  // Sueño reparador: 7-9h sin interrupciones
    GOOD,       // Sueño adecuado con leves interrupciones esporádicas
    FAIR,       // Sueño parcialmente reparador con dificultades frecuentes
    POOR,       // Sueño deficiente: cansancio diurno persistente
    INSOMNIA    // Insomnio clínico: incapacidad para conciliar o mantener el sueño
}
