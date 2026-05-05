package com.amachi.app.vitalia.medicalcatalog.diagnosis.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a new ICD-10 diagnosis is created in the catalog.
 */
@Getter
@RequiredArgsConstructor
public class Icd10ChangedEvent extends DomainEvent {

    private final Long diagnosisId;
    private final String code;
    private final String description;

}
