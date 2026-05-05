package com.amachi.app.core.common.error;

import lombok.Getter;

public enum ErrorCode {

    // --- VALIDATION ERRORS ---
    VAL_INVALID_FORMAT("VAL_001", "Invalid parameter format"),
    VAL_REQUIRED_FIELD("VAL_002", "Required field missing"),
    VAL_INVALID_TYPE("VAL_003", "Invalid data type"),
    VAL_OUT_OF_RANGE("VAL_004", "Value out of allowed range"),
    VAL_INVALID_EMAIL("VAL_005", "Invalid email format"),
    VAL_INVALID_DATE("VAL_006", "Invalid date format"),
    VALIDATION_ERROR("VAL_007", "Validation failed"),

    // --- BUSINESS ERRORS ---
    BUS_RESOURCE_NOT_FOUND("BUS_001", "Requested resource not found"),
    BUS_DUPLICATE_RESOURCE("BUS_002", "Resource already exists"),
    BUS_INVALID_STATE("BUS_003", "Invalid business state"),
    BUS_INSUFFICIENT_PERMISSIONS("BUS_004", "Insufficient permissions"),
    BUS_OPERATION_NOT_ALLOWED("BUS_005", "Operation not allowed"),
    BUS_RULE_VIOLATION("BUS_006", "Business rule violation"),

    // --- SYSTEM ERRORS ---
    SYS_INTERNAL_ERROR("SYS_001", "Internal server error"),
    SYS_SERVICE_UNAVAILABLE("SYS_002", "Service temporarily unavailable"),
    SYS_DATABASE_ERROR("SYS_003", "Database operation failed"),
    SYS_EXTERNAL_SERVICE_ERROR("SYS_004", "External service error"),
    SYS_TIMEOUT("SYS_005", "Request timeout"),

    // --- SECURITY ERRORS ---
    SEC_UNAUTHORIZED("SEC_001", "Authentication required"),
    SEC_FORBIDDEN("SEC_002", "Access forbidden"),
    SEC_INVALID_TOKEN("SEC_003", "Invalid authentication token"),
    SEC_TOKEN_EXPIRED("SEC_004", "Authentication token expired"),
    SEC_ACCOUNT_ACTIVATION_FAILED("SEC_005", "Account activation failed"),
    SECURITY_ERROR("SEC_006", "Security error"),
    SEC_INVALID_OPERATION("SEC_007", "Invalid security operation"),
    SEC_TENANT_NOT_FOUND("SEC_008", "Tenant not found"),
    SEC_AUTHENTICATION_ERROR("SEC_009", "Authentication process failed"),
    SEC_USER_NOT_FOUND("SEC_010", "User not found"),
    SEC_USER_DISABLED("SEC_011", "User account is disabled"),
    SEC_USER_LOCKED("SEC_012", "User account is locked"),
    SEC_INVALID_CREDENTIALS("SEC_013", "Invalid username or password"),
    SEC_TENANT_DISABLED("SEC_014", "Tenant account is disabled"),
    SEC_INVALID_TENANT("SEC_015", "Invalid tenant name");

    private final String code;
    private final String description;

    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
