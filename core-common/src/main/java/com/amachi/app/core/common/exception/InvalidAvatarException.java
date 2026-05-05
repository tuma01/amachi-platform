package com.amachi.app.core.common.exception;

import lombok.Getter;

@Getter
public class InvalidAvatarException extends RuntimeException {

    private final String key;
    private final Object[] args;
    private final String entityName;

    public InvalidAvatarException(String message) {
        super(message);
        this.key = message;
        this.args = null;
        this.entityName = "Avatar";
    }

    public InvalidAvatarException(String key, Object... args) {
        super(key);
        this.key = key;
        this.args = args;
        this.entityName = "Avatar";
    }

    public InvalidAvatarException(String message, Throwable cause) {
        super(message, cause);
        this.key = message;
        this.args = null;
        this.entityName = "Avatar";
    }
}
