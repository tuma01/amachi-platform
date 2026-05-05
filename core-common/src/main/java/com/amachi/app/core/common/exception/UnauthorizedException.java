package com.amachi.app.core.common.exception;

import com.amachi.app.core.common.error.ErrorCode;
import lombok.Getter;

/**
 * Exception thrown when a user is not authorized to perform an action or access a resource.
 */
@Getter
public class UnauthorizedException extends CoreException {

    private final String resource;
    private final Object identifier;

    public UnauthorizedException(String resource, String messageKey, Object identifier) {
        super(ErrorCode.SEC_UNAUTHORIZED, messageKey, new Object[]{resource, identifier});
        this.resource = resource;
        this.identifier = identifier;
    }

    public UnauthorizedException(String messageKey) {
        super(ErrorCode.SEC_UNAUTHORIZED, messageKey, null);
        this.resource = "N/A";
        this.identifier = null;
    }
}
