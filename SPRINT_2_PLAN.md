# Sprint 2 — Security & Compliance Hardening
**Rama:** `feature/AMA-027`  
**Fecha inicio:** 2026-05-07  
**Estado:** PENDIENTE — Sprint 1 completado, app funcionando

---

## Contexto

Sprint 1 completado (AMA-027):
- ✅ Swagger protegido en producción (9.3)
- ✅ Rate Limiting `/auth/login` + `/auth/refresh` — 10 req/min por IP (9.4)
- ✅ PHI Encryption AES-256 en campo `NHC` de `MED_PATIENT` (9.5)
- ✅ Logback rolling 90 días + MDC `tenantCode`/`userId`/`username` (9.6)
- ✅ Avatar migrado a `core-management` (único dueño)
- ✅ TenantFactory deduplicado en `core-common`
- ✅ Todas las tablas `_aud` Envers creadas vía Flyway (V10_10, V10_20, V10_42)
- ✅ App arrancando correctamente con `ddl-auto: validate`

---

## TAREA 1 — Envers RevisionEntity (ALTA PRIORIDAD)

### Qué es y por qué
Actualmente `REVINFO` solo tiene `REV` (bigint) y `REVTSTMP` (bigint timestamp).
Con `@RevisionEntity` podemos añadir quién hizo el cambio y en qué tenant.

Resultado: cada fila `*_aud` tendrá trazabilidad completa:
- **Qué** cambió (columnas de la entidad)
- **Cuándo** (REVTSTMP)
- **Quién** (userId, username)
- **En qué tenant** (tenantCode)

Esto es requerido para ISO 27001 A.12.4 (logging) e HIPAA §164.312(b) (audit controls).

### Implementación

#### 1. Actualizar tabla REVINFO vía Flyway

Añadir columnas en un nuevo script (módulo `core-domain`):

```sql
-- VX_Y__alter_revinfo_add_audit_fields.sql
ALTER TABLE REVINFO
    ADD COLUMN USER_ID     BIGINT       NULL,
    ADD COLUMN TENANT_CODE VARCHAR(50)  NULL,
    ADD COLUMN USERNAME    VARCHAR(100) NULL;
```

#### 2. Crear `AuditRevisionEntity`

Archivo: `core-auth/src/main/java/com/amachi/app/core/auth/auditevent/AuditRevisionEntity.java`

```java
@Entity
@RevisionEntity(AuditRevisionListener.class)
@Table(name = "REVINFO")
public class AuditRevisionEntity extends DefaultRevisionEntity {

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "TENANT_CODE", length = 50)
    private String tenantCode;

    @Column(name = "USERNAME", length = 100)
    private String username;
    // getters/setters
}
```

#### 3. Crear `AuditRevisionListener`

Archivo: `core-auth/src/main/java/com/amachi/app/core/auth/auditevent/AuditRevisionListener.java`

```java
public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevisionEntity rev = (AuditRevisionEntity) revisionEntity;

        // Leer del SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AuthContext ctx) {
            rev.setUserId(ctx.getUserId());
            rev.setTenantCode(ctx.getTenantCode());
            rev.setUsername(auth.getName());
        }
        // Fallback: leer del TenantContext
        if (rev.getTenantCode() == null) {
            rev.setTenantCode(TenantContext.getTenantCode());
        }
    }
}
```

### Archivos a crear/modificar
- `core-domain/src/main/resources/db/migration/VX__alter_revinfo_add_audit_fields.sql` (nuevo)
- `core-auth/.../auditevent/AuditRevisionEntity.java` (nuevo)
- `core-auth/.../auditevent/AuditRevisionListener.java` (nuevo)

### Precaución
- El `RevisionListener` NO puede tener dependencias Spring (`@Autowired`) — usa contextos estáticos (`SecurityContextHolder`, `TenantContext`)
- Verificar que `REVINFO` sea la misma tabla en todos los módulos (ya confirmado: `V3_9__create_dmn_person_aud.sql`)

---

## TAREA 5 — Global Validation Handler (MEDIA PRIORIDAD)

### Qué es y por qué
Actualmente, si una petición falla la validación de `@Valid` (e.g. campo obligatorio nulo), Spring devuelve un JSON genérico de error 400 que no sigue el formato `ApiResponse<ErrorDetail>` estándar de la app.

El `GlobalValidationHandler` captura `MethodArgumentNotValidException` y devuelve una respuesta estructurada con todos los campos inválidos.

### Implementación

Archivo: `vitalia-boot/src/main/java/com/amachi/app/vitalia/boot/exception/GlobalValidationHandler.java`

```java
@RestControllerAdvice
public class GlobalValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<List<FieldErrorDto>> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<FieldErrorDto> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fe -> new FieldErrorDto(fe.getField(), fe.getDefaultMessage()))
            .toList();

        return ApiResponse.error(
            HttpStatusCode.BAD_REQUEST,
            ErrorDetail.from(ErrorCode.VALIDATION_ERROR, "Datos inválidos", errors, Map.of("path", request.getRequestURI())),
            request.getRequestURI()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<List<FieldErrorDto>> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {

        List<FieldErrorDto> errors = ex.getConstraintViolations()
            .stream()
            .map(cv -> new FieldErrorDto(cv.getPropertyPath().toString(), cv.getMessage()))
            .toList();

        return ApiResponse.error(
            HttpStatusCode.BAD_REQUEST,
            ErrorDetail.from(ErrorCode.VALIDATION_ERROR, "Restricción violada", errors, Map.of("path", request.getRequestURI())),
            request.getRequestURI()
        );
    }
}
```

DTO auxiliar:
```java
public record FieldErrorDto(String field, String message) {}
```

### Archivos a crear
- `vitalia-boot/.../exception/GlobalValidationHandler.java`
- `vitalia-boot/.../exception/FieldErrorDto.java`

---

## TAREA 3 — PHI Encryption campos TEXT clínicos (BAJA PRIORIDAD)

Evaluación previa requerida antes de implementar:

### Campos candidatos
| Entidad | Campo | Impacto búsqueda |
|---|---|---|
| `Patient` | `allergySummary`, `additionalRemarks` | Bajo (no se busca por texto libre) |
| `Encounter` | `diagnosisNotes`, `clinicalNotes` | Medio (puede buscarse por palabras clave) |
| `Condition` | `symptoms`, `treatmentNotes` | Medio |
| `Observation` | `valueText` | Alto (se filtra por valor) |

### Prerequisitos
1. Confirmar que **no hay búsquedas LIKE** sobre estos campos en los repositorios
2. Confirmar que no hay datos existentes en BD que migrar
3. Usar el mismo `EncryptedStringConverter` ya creado (`core-common/.../converter/`)

### Campos **NO encriptar** (por ahora)
- `Observation.valueText` — se usa en filtros de laboratorio
- Cualquier campo usado en `@Query` con LIKE o comparaciones

---

## Issues identificados pendientes de resolver

### 1. HospitalController inyecta impl directamente
```java
// core-geography/doctor/controller/HospitalController.java
private final HospitalServiceImpl service; // ❌ debería ser HospitalService
```
Fix: Cambiar a `HospitalService` (interfaz).

### 2. DoctorHospitalAssignmentController idem
```java
private final DoctorHospitalAssignmentServiceImpl service; // ❌
```
Fix: Cambiar a `DoctorHospitalAssignmentService`.

### 3. Varios controllers en vitalia-medical-core inyectan impl
Patrón recurrente detectado durante AMA-027. Revisar todos los controllers para que usen la interfaz de servicio.

---

## Recordatorios de arquitectura

- **Regla de oro**: Controllers inyectan interfaz de servicio, nunca `ServiceImpl`
- **Envers + relaciones a no-auditados**: usar `@Audited(targetAuditMode = NOT_AUDITED)` en el campo `@ManyToOne`
- **@Lob byte[]**: siempre usar `columnDefinition = "LONGBLOB"` para evitar mismatch de tipo con MySQL (Hibernate 6 infiere `tinyblob` por defecto)
- **@ElementCollection en entidades @Audited**: marcar con `@NotAudited` si no se necesita historial de la colección
- **REV en _aud tables**: siempre `BIGINT` (no `INT`) para coincidir con `REVINFO.REV BIGINT`
- **EXTERNAL_ID**: definido en `Auditable` como `VARCHAR(36)`. No redefinir en subclases

---

*Generado: 2026-05-07 — AMA-027 Sprint 1 completado, Sprint 2 pendiente*
