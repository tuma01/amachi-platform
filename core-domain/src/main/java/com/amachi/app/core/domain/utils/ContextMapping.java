package com.amachi.app.core.domain.utils;

import com.amachi.app.core.common.enums.DomainContext;
import com.amachi.app.core.common.enums.RoleContext;
import org.springframework.stereotype.Component;

/**
 * Utility component to manage the mapping between Security Roles and Domain Entities.
 * This is the SINGLE SOURCE OF TRUTH for the Identity vs Role architecture.
 * Moved from core-common to core-domain as it contains business logic.
 */
@Component
public class ContextMapping {

    /**
     * Maps a Security RoleContext (UX/Session) to a DomainContext (Persistence).
     * Determines if a specialized clinical/business entity must be created.
     * 
     * @param roleContext The security/UX context of the user.
     * @return The corresponding DomainContext, or null if no specialized domain entity is required.
     */
    public DomainContext toDomain(RoleContext roleContext) {
        if (roleContext == null) return null;

        return switch (roleContext) {
            case DOCTOR -> DomainContext.DOCTOR;
            case PATIENT -> DomainContext.PATIENT;
            case NURSE -> DomainContext.NURSE;
            case ADMIN -> DomainContext.ADMIN;
            case SUPER_ADMIN -> DomainContext.SUPER_ADMIN;
            
            // Administrative roles consolidated into the Employee entity
            case EMPLOYEE, RECEPTIONIST, LAB_TECHNICIAN -> DomainContext.EMPLOYEE;
            
            // Roles that do not require a specialized domain entity (Pure Security/Access)
            case GUEST -> null;
        };
    }
}
