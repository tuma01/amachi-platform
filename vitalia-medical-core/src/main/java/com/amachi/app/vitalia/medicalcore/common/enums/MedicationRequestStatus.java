package com.amachi.app.vitalia.medicalcore.common.enums;

/**
 * Estado del flujo de una prescripción médica (FHIR Prescription Status).
 */
public enum MedicationRequestStatus {
    ACTIVE,
    ON_HOLD,
    CANCELLED,
    COMPLETED,
    ENTERED_IN_ERROR,
    STOPPED,
    DRAFT,
    UNKNOWN
}
