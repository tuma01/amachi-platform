package com.amachi.app.vitalia.medicalcore.common.enums;

/**
 * Estado administrativo y clínico de un episodio de cuidados continuos (FHIR EpisodeOfCare).
 */
public enum EpisodeOfCareStatus {
    PLANNED,
    WAITLIST,
    ACTIVE,
    ONHOLD,
    FINISHED,
    CANCELLED,
    ENTERED_IN_ERROR
}
