package com.amachi.app.core.domain.hospital.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;

/**
 * Event published when a new Hospital is created in the domain.
 */
@Getter
public class HospitalCreatedEvent extends DomainEvent {

    private final Long hospitalId;
    private final String code;
    private final String legalName;
    private final String taxId;

    public HospitalCreatedEvent(Long hospitalId, String code, String legalName, String taxId) {
        super();
        this.hospitalId = hospitalId;
        this.code = code;
        this.legalName = legalName;
        this.taxId = taxId;
    }
}
