package com.amachi.app.core.common.api;

import com.amachi.app.core.common.error.ErrorDetail;
import com.amachi.app.core.common.http.HttpStatusCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

public class ApiResponse<T> {
    @Schema(description = "Indicates if the API call was successful", example = "true",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private final boolean success;

    @Schema(description = "The data returned by the API call", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private final T data;

    @Schema(description = "List of error details if the API call failed",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private final List<ErrorDetail> errors;

    @Schema(description = "The request path that was called", example = "/api/v1/resource",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private final String path;

    @Schema(description = "The timestamp when the response was generated", example = "2023-10-05T14:48:00Z",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private final String timestamp;

    @Schema(description = "HTTP status code of the response", example = "200",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private final int status;

    private ApiResponse(boolean success, T data, List<ErrorDetail> errors, String path, String timestamp, int status) {
        this.success = success;
        this.data = data;
        this.errors = errors;
        this.path = path;
        this.timestamp = timestamp;
        this.status = status;
    }

    public boolean isSuccess() { return success; }
    public T getData() { return data; }
    public List<ErrorDetail> getErrors() { return errors; }
    public String getPath() { return path; }
    public String getTimestamp() { return timestamp; }
    public int getStatus() { return status; }

    public static <T> ApiResponseBuilder<T> builder() {
        return new ApiResponseBuilder<>();
    }

    public static class ApiResponseBuilder<T> {
        private boolean success;
        private T data;
        private List<ErrorDetail> errors;
        private String path;
        private String timestamp;
        private int status;

        public ApiResponseBuilder<T> success(boolean success) { this.success = success; return this; }
        public ApiResponseBuilder<T> data(T data) { this.data = data; return this; }
        public ApiResponseBuilder<T> errors(List<ErrorDetail> errors) { this.errors = errors; return this; }
        public ApiResponseBuilder<T> path(String path) { this.path = path; return this; }
        public ApiResponseBuilder<T> timestamp(String timestamp) { this.timestamp = timestamp; return this; }
        public ApiResponseBuilder<T> status(int status) { this.status = status; return this; }

        public ApiResponse<T> build() {
            return new ApiResponse<>(success, data, errors, path, timestamp, status);
        }
    }

    // ==========================
    // Success Helpers
    // ==========================
    public static <T> ApiResponse<T> success(T data, String path, HttpStatusCode status) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(status.getCode())
                .data(data)
                .errors(null)
                .path(path)
                .timestamp(Instant.now().toString())
                .build();
    }

    public static <T> ApiResponse<T> success(String path) {
        return success(null, path, HttpStatusCode.OK);
    }
    public static <T> ApiResponse<T> created(T data, String path) {
        return success(data, path, HttpStatusCode.CREATED);
    }


    // --- Error responses ---
    public static <T> ApiResponse<T> error(HttpStatusCode status,
                                           String message,
                                           String reason,
                                           String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(status.getCode())
                .data(null)
                .errors(List.of(ErrorDetail.of(
                        String.valueOf(status),
                        message,
                        reason,
                        path)))
                .path(path)
                .timestamp(Instant.now().toString())
                .build();
    }

    public static <T> ApiResponse<T> error(HttpStatusCode status,
                                           ErrorDetail errorDetail,
                                           String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(status.getCode())
                .data(null)
                .errors(List.of(errorDetail))
                .path(path)
                .timestamp(Instant.now().toString())
                .build();
    }

    // Helper for business/validation errors
    public static <T> ApiResponse<T> notFound(String message, String path) {
        return error(HttpStatusCode.NOT_FOUND, message, HttpStatusCode.NOT_FOUND.getReason(), path);
    }

    public static <T> ApiResponse<T> badRequest(String message, String path) {
        return error(HttpStatusCode.BAD_REQUEST, message, HttpStatusCode.BAD_REQUEST.getReason(), path);
    }

    public static <T> ApiResponse<T> forbidden(String message, String path) {
        return error(HttpStatusCode.FORBIDDEN, message, HttpStatusCode.FORBIDDEN.getReason(), path);
    }

    public static <T> ApiResponse<T> unauthorized(String message, String path) {
        return error(HttpStatusCode.UNAUTHORIZED, message, HttpStatusCode.UNAUTHORIZED.getReason(), path);
    }

    public static <T> ApiResponse<T> conflict(String message, String path) {
        return error(HttpStatusCode.CONFLICT, message, HttpStatusCode.CONFLICT.getReason(), path);
    }

//    public static HttpStatusCode fromCode(int code) {
//        for (HttpStatusCode c : values()) {
//            if (c.getCode() == code) return c;
//        }
//        // fallback: return INTERNAL_SERVER_ERROR or throw según tu política
//        return HttpStatusCode.INTERNAL_SERVER_ERROR;
//    }
}
