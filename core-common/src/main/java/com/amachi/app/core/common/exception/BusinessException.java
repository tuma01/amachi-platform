package com.amachi.app.core.common.exception;

import com.amachi.app.core.common.error.ErrorCode;

/**
 * Excepción base para reglas de negocio.
 */
public class BusinessException extends CoreException {
    public BusinessException(String messageKey, Object... args) {
        super(ErrorCode.BUS_RULE_VIOLATION, messageKey, args);
    }
}
