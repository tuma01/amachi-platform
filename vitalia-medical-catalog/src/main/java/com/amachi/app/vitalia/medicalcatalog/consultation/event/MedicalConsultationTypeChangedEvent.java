package com.amachi.app.vitalia.medicalcatalog.consultation.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a Medical Consultation Type is changed in the catalog.
 */
@Getter
@RequiredArgsConstructor
public class MedicalConsultationTypeChangedEvent extends DomainEvent {

    private final Long consultationTypeId;
    private final String code;
    private final String name;

}
