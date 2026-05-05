package com.amachi.app.vitalia.medicalcatalog.procedure.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a new Medical Procedure is created in the catalog.
 */
@Getter
@RequiredArgsConstructor
public class MedicalProcedureChangedEvent extends DomainEvent {

    private final Long procedureId;
    private final String code;
    private final String name;

}
