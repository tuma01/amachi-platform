package com.amachi.app.core.common.exception;

import com.amachi.app.core.common.error.ErrorCode;
import lombok.Getter;

/**
 * Standard Exception for resource not found scenarios.
 */
@Getter
public class NotFoundException extends CoreException {

    private final String entityName;
    private final Object identifier;

    public NotFoundException(String entityName, Object identifier) {
        super(ErrorCode.BUS_RESOURCE_NOT_FOUND, "err.not_found", new Object[]{entityName, identifier});
        this.entityName = entityName;
        this.identifier = identifier;
    }

    public NotFoundException(String messageKey) {
        super(ErrorCode.BUS_RESOURCE_NOT_FOUND, messageKey, null);
        this.entityName = "Unknown";
        this.identifier = null;
    }
}
