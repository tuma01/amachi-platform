package com.amachi.app.core.common.exception;

import com.amachi.app.core.common.error.ErrorCode;
import lombok.Getter;

/**
 * Base core exception (Clean Architecture - No Framework dependencies).
 * SaaS Elite Tier: Soporta códigos de error y argumentos para internacionalización.
 */
@Getter
public class CoreException extends RuntimeException {

    private final ErrorCode errorCode;
    protected final Object[] args;

    public CoreException(String message) {
        super(message);
        this.errorCode = ErrorCode.SYS_INTERNAL_ERROR;
        this.args = null;
    }

    public CoreException(ErrorCode errorCode, String messageKey, Object[] args) {
        super(messageKey);
        this.errorCode = errorCode;
        this.args = args;
    }
}
