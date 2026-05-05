package com.amachi.app.vitalia.medicalcore.common.enums;

/**
 * Estado del ciclo de vida de una observación clínica (FHIR Observation Status).
 */
public enum ObservationStatus {
    REGISTERED,
    PRELIMINARY,
    FINAL,
    AMENDED,
    CORRECTED,
    CANCELLED,
    ENTERED_IN_ERROR,
    UNKNOWN
}
