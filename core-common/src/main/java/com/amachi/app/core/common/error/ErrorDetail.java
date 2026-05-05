package com.amachi.app.core.common.error;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ErrorDetail {

    private final ErrorCategory category;
    private final String code;
    private final String userMessage;
    private final String developerMessage;
    private final String field;
    private final Map<String, Object> details;
    private final String timestamp;
    private final String traceId;

    private ErrorDetail(ErrorCategory category, String code, String userMessage, String developerMessage, String field, Map<String, Object> details, String timestamp, String traceId) {
        this.category = category;
        this.code = code;
        this.userMessage = userMessage;
        this.developerMessage = developerMessage;
        this.field = field;
        this.details = details;
        this.timestamp = timestamp;
        this.traceId = traceId;
    }

    public ErrorCategory getCategory() { return category; }
    public String getCode() { return code; }
    public String getUserMessage() { return userMessage; }
    public String getDeveloperMessage() { return developerMessage; }
    public String getField() { return field; }
    public Map<String, Object> getDetails() { return details; }
    public String getTimestamp() { return timestamp; }
    public String getTraceId() { return traceId; }

    public static ErrorDetailBuilder builder() {
        return new ErrorDetailBuilder();
    }

    public static class ErrorDetailBuilder {
        private ErrorCategory category;
        private String code;
        private String userMessage;
        private String developerMessage;
        private String field;
        private Map<String, Object> details;
        private String timestamp;
        private String traceId;

        public ErrorDetailBuilder category(ErrorCategory category) { this.category = category; return this; }
        public ErrorDetailBuilder code(String code) { this.code = code; return this; }
        public ErrorDetailBuilder userMessage(String userMessage) { this.userMessage = userMessage; return this; }
        public ErrorDetailBuilder developerMessage(String developerMessage) { this.developerMessage = developerMessage; return this; }
        public ErrorDetailBuilder field(String field) { this.field = field; return this; }
        public ErrorDetailBuilder details(Map<String, Object> details) { this.details = details; return this; }
        public ErrorDetailBuilder timestamp(String timestamp) { this.timestamp = timestamp; return this; }
        public ErrorDetailBuilder traceId(String traceId) { this.traceId = traceId; return this; }

        public ErrorDetail build() {
            return new ErrorDetail(category, code, userMessage, developerMessage, field, details, timestamp, traceId);
        }
    }

    /**
     * Construcción genérica simple usada en ApiResponse.error(...)
     */
    public static ErrorDetail of(String code, String userMessage, String developerMessage, String path) {
        return ErrorDetail.builder()
                .category(ErrorCategory.GENERAL)
                .code(code)
                .userMessage(userMessage)
                .developerMessage(developerMessage)
                .details(Map.of("path", path))
                .timestamp(Instant.now().toString())
                .traceId(UUID.randomUUID().toString())
                .build();
    }

    /**
     * Construcción avanzada con ErrorCode estándar
     */
    public static ErrorDetail from(ErrorCode errorCode, String userMessage, String field, Map<String, Object> details) {
        return ErrorDetail.builder()
                .category(determineCategory(errorCode))
                .code(errorCode.getCode())
                .userMessage(userMessage != null ? userMessage : errorCode.getDescription())
                .developerMessage(errorCode.getDescription())
                .field(field)
                .details(details != null ? details : new HashMap<>())
                .timestamp(Instant.now().toString())
                .traceId(UUID.randomUUID().toString())
                .build();
    }

    private static ErrorCategory determineCategory(ErrorCode errorCode) {
        if (errorCode == null) return ErrorCategory.GENERAL;
        String code = errorCode.getCode();
        if (code.startsWith("VAL_")) return ErrorCategory.VALIDATION;
        if (code.startsWith("BUS_")) return ErrorCategory.BUSINESS;
        if (code.startsWith("SYS_")) return ErrorCategory.SYSTEM;
        if (code.startsWith("SEC_")) return ErrorCategory.SECURITY;
        return ErrorCategory.GENERAL;
    }
}
