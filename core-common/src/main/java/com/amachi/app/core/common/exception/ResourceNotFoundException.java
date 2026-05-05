package com.amachi.app.core.common.exception;

import com.amachi.app.core.common.error.ErrorCode;
import lombok.Getter;

/**
 * Excepción profesional para recursos no encontrados.
 */
@Getter
public class ResourceNotFoundException extends CoreException {

    private final String entityName;

    public ResourceNotFoundException(String entityName, String messageKey, Object... args) {
        super(ErrorCode.BUS_RESOURCE_NOT_FOUND, messageKey, args);
        this.entityName = entityName;
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.entityName = "Unknown";
    }

//    public ResourceNotFoundException(String key, String field, Object... args) {
//        super(key);
//        this.key = key;
//        this.field = field;
//        this.args = args;
//    }
}
