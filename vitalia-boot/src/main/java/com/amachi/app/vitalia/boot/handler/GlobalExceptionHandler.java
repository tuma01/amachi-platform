package com.amachi.app.vitalia.boot.handler;

import com.amachi.app.core.auth.exception.*;
import com.amachi.app.core.common.api.ApiResponse;
import com.amachi.app.core.common.error.ErrorCode;
import com.amachi.app.core.common.error.ErrorDetail;
import com.amachi.app.core.common.http.HttpStatusCode;
import com.amachi.app.core.common.i18n.Translator;
import com.amachi.app.core.common.exception.BadRequestException;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.exception.ResourceNotFoundException;
import com.amachi.app.core.common.exception.InvalidAvatarException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.util.NestedServletException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.Collectors;

@Hidden
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "com.amachi.app")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // =====================================
    // === VALIDATION EXCEPTIONS (JSON y @Valid)
    // =====================================

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            org.springframework.http.HttpStatusCode status,
            WebRequest request) {

        Map<String, String> invalidFields = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> Translator.toLocale(fe.getDefaultMessage(), fe.getArguments()),
                        (oldVal, newVal) -> oldVal + "; " + newVal));

        ErrorDetail errorDetail = ErrorDetail.from(
                ErrorCode.VAL_REQUIRED_FIELD,
                Translator.toLocale("validation.error.fields", null),
                null,
                Map.of(
                        "errorCount", invalidFields.size(),
                        "invalidFields", invalidFields));

        return buildErrorResponseObject(HttpStatusCode.BAD_REQUEST, errorDetail, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            org.springframework.http.HttpStatusCode status,
            WebRequest request) {

        String fieldName = null;

        if (ex.getCause() instanceof com.fasterxml.jackson.databind.JsonMappingException jsonEx) {
            fieldName = jsonEx.getPath().stream()
                    .map(com.fasterxml.jackson.databind.JsonMappingException.Reference::getFieldName)
                    .collect(Collectors.joining("."));
        }

        ErrorDetail errorDetail = ErrorDetail.from(
                ErrorCode.VAL_INVALID_FORMAT,
                Translator.toLocale("validation.error.fields", null),
                fieldName,
                Map.of("info", ex.getMostSpecificCause() != null
                        ? ex.getMostSpecificCause().getMessage()
                        : ex.getMessage()));

        return buildErrorResponseObject(HttpStatusCode.BAD_REQUEST, errorDetail, request);
    }

    private ResponseEntity<Object> buildErrorResponseObject(
            HttpStatusCode status,
            ErrorDetail detail,
            WebRequest request) {

        HttpStatusCode safeStatus = (status != null)
                ? status
                : HttpStatusCode.INTERNAL_SERVER_ERROR;

        String path = (request != null)
                ? request.getDescription(false).replace("uri=", "")
                : "N/A";

        return ResponseEntity
                .status(HttpStatusMapper.toSpringStatus(safeStatus))
                .body(ApiResponse.error(safeStatus, detail, path));
    }

    // =====================================
    // === BUSINESS EXCEPTIONS
    // =====================================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex,
            HttpServletRequest request) {
        Map<String, Object> details = Map.of(
                "id", ex.getArgs()[0],
                "entity", ex.getEntityName());
        ErrorDetail detail = ErrorDetail.from(
                ErrorCode.BUS_RESOURCE_NOT_FOUND,
                Translator.toLocale(ex.getMessage(), ex.getArgs()),
                null,
                details);
        // 🔹 construimos primero el response manualmente
        ResponseEntity<ApiResponse<Void>> responseEntity = buildErrorResponse(HttpStatusCode.NOT_FOUND, detail,
                request);

        // 🔹 registramos logs claros antes de devolverlo
        log.error("🔥 [GlobalExceptionHandler] Capturada ResourceNotFoundException: {}", ex.getMessage());
        log.error("🔥 [GlobalExceptionHandler] ResponseEntity devuelto: {}", responseEntity);
        log.error("🔥 [GlobalExceptionHandler] HTTP Status esperado: {}", HttpStatusCode.NOT_FOUND);
        return responseEntity;
        // return buildErrorResponse(HttpStatusCode.NOT_FOUND, detail, request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        ErrorDetail detail = ErrorDetail.from(
                ErrorCode.VAL_INVALID_FORMAT,
                Translator.toLocale(ex.getMessage(), ex.getArgs()),
                ex.getField(),
                Map.of("info", ex.getMessage()));
        return buildErrorResponse(HttpStatusCode.BAD_REQUEST, detail, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException ex,
            HttpServletRequest request) {
        String messageKey = ex.getMessage() != null && ex.getMessage().contains("duplicate")
                ? "error.data.integrity.violation.value"
                : "error.data.integrity.violation.generic";

        ErrorDetail detail = ErrorDetail.from(
                ErrorCode.BUS_DUPLICATE_RESOURCE,
                Translator.toLocale(messageKey, null),
                null,
                Map.of("info", ex.getMostSpecificCause().getMessage()));

        return buildErrorResponse(HttpStatusCode.CONFLICT, detail, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex,
            HttpServletRequest request) {
        ErrorDetail detail = ErrorDetail.from(
                ErrorCode.BUS_RULE_VIOLATION,
                Translator.toLocale(ex.getMessage(), null),
                null, 
                Map.of("category", "BUSINESS_RULE"));
        return buildErrorResponse(HttpStatusCode.CONFLICT, detail, request);
    }

    @ExceptionHandler(InvalidAvatarException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidAvatarException(InvalidAvatarException ex,
            HttpServletRequest request) {
        ErrorDetail detail = ErrorDetail.from(
                ErrorCode.VALIDATION_ERROR,
                Translator.toLocale(ex.getMessage(), ex.getArgs()),
                ex.getEntityName(),
                Map.of("info", ex.getMessage()));
        return buildErrorResponse(HttpStatusCode.BAD_REQUEST, detail, request);
    }

    // =====================================
    // === SECURITY EXCEPTIONS
    // =====================================
    @ExceptionHandler({
            AuthenticationException.class,
            TokenExpiredException.class,
            InvalidTokenException.class,
            UnauthorizedException.class,
            ForbiddenException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleSecurityExceptions(Exception ex, HttpServletRequest request) {
        HttpStatusCode status;
        ErrorCode errorCode;

        if (ex instanceof AuthenticationException) {
            status = HttpStatusCode.UNAUTHORIZED;
            errorCode = ErrorCode.SEC_INVALID_TOKEN;
        } else if (ex instanceof TokenExpiredException) {
            status = HttpStatusCode.FORBIDDEN;
            errorCode = ErrorCode.SEC_TOKEN_EXPIRED;
        } else if (ex instanceof InvalidTokenException) {
            status = HttpStatusCode.BAD_REQUEST;
            errorCode = ErrorCode.SEC_INVALID_TOKEN;
        } else if (ex instanceof UnauthorizedException) {
            status = HttpStatusCode.UNAUTHORIZED;
            errorCode = ErrorCode.SEC_UNAUTHORIZED;
        } else { // ForbiddenException
            status = HttpStatusCode.FORBIDDEN;
            errorCode = ErrorCode.SEC_FORBIDDEN;
        }

        ErrorDetail detail = ErrorDetail.from(
                errorCode,
                Translator.toLocale(ex.getMessage(), null),
                null,
                Map.of("info", ex.getMessage()));

        return buildErrorResponse(status, detail, request);
    }

    @ExceptionHandler(AppSecurityException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppSecurityException(AppSecurityException ex,
            HttpServletRequest request) {
        ErrorDetail detail = ErrorDetail.from(
                ex.getErrorCode(),
                Translator.toLocale(ex.getMessage(), ex.getArgs()),
                null,
                Map.of("info", ex.getMessage()));
        return buildErrorResponse(HttpStatusCode.BAD_REQUEST, detail, request);
    }

    @ExceptionHandler(NestedServletException.class)
    public ResponseEntity<ApiResponse<Void>> handleNestedServletException(NestedServletException ex,
            HttpServletRequest request) {
        Throwable root = ex.getRootCause();

        // Si la causa real es una de tus excepciones personalizadas, la reenvías al
        // handler correspondiente
        if (root instanceof ResourceNotFoundException resourceNotFound) {
            return handleResourceNotFound(resourceNotFound, request);
        }
        if (root instanceof AppSecurityException appSecEx) {
            return handleAppSecurityException(appSecEx, request);
        }

        // Si no reconoces la causa, devuelves un error genérico
        ErrorDetail detail = ErrorDetail.from(
                ErrorCode.SYS_INTERNAL_ERROR,
                "Unexpected server error",
                ex.getMessage(),
                Map.of("cause", root != null ? root.getClass().getName() : "unknown"));

        return buildErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, detail, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("❌ Unexpected exception: ", ex);

        Throwable root = ex;
        // 🔍 Desenrollar InvocationTargetException, RuntimeException, etc.
        while (root.getCause() != null && root != root.getCause()) {
            root = root.getCause();
        }

        // ⚡ Si la causa real es ResourceNotFoundException, delegar al handler
        // correspondiente
        if (root instanceof ResourceNotFoundException resourceEx) {
            return handleResourceNotFound(resourceEx, request);
        }

        // ⚡ Si la causa real es AppSecurityException, también delegar
        if (root instanceof AppSecurityException securityEx) {
            return handleAppSecurityException(securityEx, request);
        }

        ErrorDetail detail = ErrorDetail.from(
                ErrorCode.SYS_INTERNAL_ERROR,
                "Unexpected server error",
                ex.getMessage(),
                Map.of("cause", root != null ? root.getClass().getName() : "unknown"));
        return buildErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, detail, request);
    }

    @ExceptionHandler(InvocationTargetException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvocationTarget(InvocationTargetException ex,
            HttpServletRequest request) {
        Throwable cause = ex.getTargetException();

        if (cause instanceof ResourceNotFoundException resourceEx) {
            return handleResourceNotFound(resourceEx, request);
        }

        if (cause instanceof AppSecurityException securityEx) {
            return handleAppSecurityException(securityEx, request);
        }

        // Si es otra excepción, delega al handler genérico
        return handleGenericException(cause instanceof Exception e ? e : new RuntimeException(cause), request);
    }

    // =====================================
    // === GENERIC EXCEPTIONS
    // =====================================
    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex,
    // HttpServletRequest request) {
    // log.error("Unexpected exception: ", ex);
    //
    // ErrorDetail detail = ErrorDetail.from(
    // ErrorCode.SYS_INTERNAL_ERROR,
    // Translator.toLocale("error.internal.server", null),
    // null,
    // Map.of("exception", ex.getClass().getSimpleName(), "info", ex.getMessage())
    // );
    //
    // return buildErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, detail,
    // request);
    // }

    // =====================================
    // === HELPER
    // =====================================
    private ResponseEntity<ApiResponse<Void>> buildErrorResponse(HttpStatusCode status, ErrorDetail detail,
            HttpServletRequest request) {
        return ResponseEntity.status(status.getCode())
                .body(ApiResponse.<Void>error(status, detail, request.getRequestURI()));
    }
}
