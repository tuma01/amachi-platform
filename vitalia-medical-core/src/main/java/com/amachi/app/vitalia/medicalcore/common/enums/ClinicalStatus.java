package com.amachi.app.vitalia.medicalcore.common.enums;

/**
 * Estado clínico de una condición según estándares HL7/FHIR (Active, Inactive, Resolved).
 */
public enum ClinicalStatus {
    ACTIVE,
    INACTIVE,
    RESOLVED,
    REMITTED,
    RELAPSE
}
