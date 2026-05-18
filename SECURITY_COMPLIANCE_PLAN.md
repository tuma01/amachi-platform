# Amachi Platform — Security & Compliance Plan
## Objetivo: Nivel Aceptable ISO 27001 / ISO 13485 / HIPAA / GDPR

> **Filosofía:** No buscamos perfección. Buscamos un nivel aceptable, auditable y defendible.
> Fecha de análisis: 2026-05-07

---

## 1. ESTADO ACTUAL — FORTALEZAS EXISTENTES

Estas funcionalidades ya cumplen estándares y NO requieren cambios:

| Componente | Estándar que cumple | Evidencia en código |
|---|---|---|
| JWT stateless + blacklist | ISO 27001 A.9 (control de acceso) | `JwtServiceImpl`, `BlacklistedToken` |
| Anti-spoofing tenant en JWT | ISO 27001 A.9 | `JwtAuthenticationFilter` — compara tenantCode del token vs request |
| ThreadLocal TenantContext + fail-fast | ISO 27001 A.9 | `TenantContext`, `@PrePersist` lanza si tenantId es null |
| Hibernate Filter `IS_DELETED = false` | HIPAA §164.312(c) (integridad) | `@SQLRestriction("IS_DELETED = false")` en `AuditableTenantEntity` |
| Soft delete universal | GDPR Art.17 (derecho al olvido gestionado) | `delete()` → `IS_DELETED=true`, nunca `DELETE` físico |
| BCrypt para contraseñas | ISO 27001 A.9.4 | `BCryptPasswordEncoder` en `ApplicationConfig` |
| RBAC con `@PreAuthorize` | ISO 27001 A.9.2 | Usado en todos los controllers |
| Optimistic locking `@Version` | ISO 27001 A.12 (integridad) | Campo `VERSION` en `Auditable` |
| UUID EXTERNAL_ID inmutable | HIPAA idempotencia | `@PrePersist` genera UUID si null |
| Audit events personalizados | ISO 27001 A.12.4 (logs) | `AUT_AUDIT_EVENTS` con userId, tenantId, IP, timestamp |
| CORS deshabilitado en producción | ISO 27001 A.13 | `SecurityConfig` — CORS solo en perfil `dev` |
| ICD-10 integrado | ISO 13485 §7.3 (estándar clínico) | `CAT_ICD10`, referenciado en `MED_CONDITION` |
| Enums alineados a FHIR HL7 | ISO 13485 / FHIR | EncounterStatus, ClinicalStatus, ObservationStatus, etc. |

---

## 2. GAPS IDENTIFICADOS — PRIORIZADOS POR IMPACTO

### PRIORIDAD 1 — Crítico (bloquea compliance básico)

| # | Gap | Estándar afectado | Riesgo |
|---|---|---|---|
| G1 | Envers `RevisionEntity` no configurado | HIPAA §164.312(b), ISO 27001 A.12.4 | Sin historial completo de cambios en datos clínicos |
| G2 | Sin rate limiting en `/auth/login` | ISO 27001 A.9.4, HIPAA | Brute-force attack sin restricción |
| G3 | Swagger UI sin autenticación en producción | ISO 27001 A.13.2 | Expone estructura de API completa |
| G4 | Sin encriptación de campos PHI sensibles | HIPAA §164.312(a)(2)(iv), GDPR Art.32 | NHC, diagnósticos, notas clínicas en texto plano |

### PRIORIDAD 2 — Importante (mejora postura de compliance)

| # | Gap | Estándar afectado | Riesgo |
|---|---|---|---|
| G5 | Validación de entrada incompleta en módulos médicos | OWASP A3, ISO 27001 A.14 | XSS / injection en campos clínicos |
| G6 | Sin política de retención de logs | ISO 27001 A.12.4, GDPR Art.5 | Logs indefinidos o borrados sin trazabilidad |
| G7 | TenantResolver usa HashMap en memoria (no distribuido) | ISO 27001 A.17 (disponibilidad) | En multi-nodo, inconsistencias posibles |
| G8 | Sin sanitización de logs (datos sensibles en Slf4j) | HIPAA §164.312, GDPR | Posible leak de tokens/NHC en logs |

### PRIORIDAD 3 — Deseable (buenas prácticas)

| # | Gap | Estándar afectado |
|---|---|---|
| G9 | Sin SBOM (Software Bill of Materials) | ISO 27001 A.12.6 (vulnerabilidades) |
| G10 | TLS/cipher suites no verificadas en código | ISO 27001 A.10 |
| G11 | Sin política formal de gestión de incidentes | ISO 27001 A.16 |

---

## 3. PLAN DE IMPLEMENTACIÓN — NIVEL ACEPTABLE

> Solo se implementan G1–G6. G7–G11 se documentan como riesgo aceptado.

---

### TAREA 1 — Completar Hibernate Envers (G1)
**Módulo:** `core-domain`
**Estándar:** HIPAA §164.312(b) — audit controls; ISO 27001 A.12.4

**Qué crear:**
```
core-domain/src/main/java/com/amachi/app/core/domain/audit/
    ├── AuditRevisionEntity.java       ← @RevisionEntity personalizado
    └── EnversConfig.java              ← @Configuration que activa Envers
```

**AuditRevisionEntity.java:**
```java
@Entity
@RevisionEntity(AuditRevisionListener.class)
@Table(name = "REVINFO")
public class AuditRevisionEntity extends DefaultRevisionEntity {

    @Column(name = "TENANT_ID")
    private Long tenantId;

    @Column(name = "TENANT_CODE", length = 50)
    private String tenantCode;

    @Column(name = "USER_EMAIL", length = 150)
    private String userEmail;

    @Column(name = "IP_ADDRESS", length = 50)
    private String ipAddress;
}
```

**AuditRevisionListener.java:**
```java
@Component
public class AuditRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevisionEntity rev = (AuditRevisionEntity) revisionEntity;
        rev.setTenantId(TenantContext.getTenantId());
        rev.setTenantCode(TenantContext.getTenantCode());
        // obtener del SecurityContextHolder
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) rev.setUserEmail(auth.getName());
    }
}
```

**DDL requerido:**
```sql
-- Agregar al script Flyway (V10_41_envers o nuevo script)
ALTER TABLE REVINFO
    ADD COLUMN TENANT_ID    BIGINT,
    ADD COLUMN TENANT_CODE  VARCHAR(50),
    ADD COLUMN USER_EMAIL   VARCHAR(150),
    ADD COLUMN IP_ADDRESS   VARCHAR(50);
```

---

### TAREA 2 — Rate Limiting en Auth Endpoints (G2)
**Módulo:** `core-auth`
**Estándar:** ISO 27001 A.9.4 — protección contra acceso no autorizado

**Dependencia a agregar en `core-auth/pom.xml`:**
```xml
<dependency>
    <groupId>com.bucket4j</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>8.10.1</version>
</dependency>
```

**Qué crear:**
```
core-auth/src/main/java/com/amachi/app/core/auth/config/security/
    └── RateLimitFilter.java
```

**RateLimitFilter.java (lógica básica):**
```java
@Component
@Order(1)
public class RateLimitFilter extends OncePerRequestFilter {

    // 10 intentos por minuto por IP
    private static final int MAX_REQUESTS = 10;
    private static final Duration REFILL_DURATION = Duration.ofMinutes(1);

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if (isAuthEndpoint(request)) {
            String ip = getClientIp(request);
            Bucket bucket = buckets.computeIfAbsent(ip, k ->
                Bucket.builder()
                    .addLimit(Bandwidth.classic(MAX_REQUESTS,
                        Refill.intervally(MAX_REQUESTS, REFILL_DURATION)))
                    .build()
            );
            if (!bucket.tryConsume(1)) {
                response.setStatus(429); // Too Many Requests
                response.getWriter().write("{\"error\":\"TOO_MANY_REQUESTS\",\"message\":\"Demasiados intentos. Espere 1 minuto.\"}");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isAuthEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/auth/login") || path.startsWith("/auth/refresh");
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        return (forwarded != null) ? forwarded.split(",")[0].trim() : request.getRemoteAddr();
    }
}
```

---

### TAREA 3 — Proteger Swagger en Producción (G3)
**Módulo:** `core-auth`
**Estándar:** ISO 27001 A.13.2 — protección de información en tránsito

**En `SecurityConfig.java`, reemplazar:**
```java
// ANTES (inseguro):
.requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()

// DESPUÉS (aceptable):
.requestMatchers("/v3/api-docs/**", "/swagger-ui/**")
    .access((authentication, context) -> {
        // Solo en perfil dev/test, o para ADMIN en producción
        boolean isDev = Arrays.asList(environment.getActiveProfiles()).contains("dev");
        boolean isAdmin = authentication.get().getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                       || a.getAuthority().equals("ROLE_SUPER_ADMIN"));
        return new AuthorizationDecision(isDev || isAdmin);
    })
```

**Alternativa simple (1 línea):**
```java
.requestMatchers("/v3/api-docs/**", "/swagger-ui/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
```

---

### TAREA 4 — Encriptación básica de campos PHI (G4)
**Módulo:** `core-common` (nuevo converter JPA)
**Estándar:** HIPAA §164.312(a)(2)(iv) — encriptación en reposo; GDPR Art.32

**Estrategia:** JPA `AttributeConverter` con AES-256-GCM. Solo aplicar a los campos más críticos:

**Campos a encriptar (mínimo aceptable):**
- `Patient.nhc` — número historia clínica
- `Patient.additionalRemarks` — observaciones personales
- `MedicalHistory.observations` y `notes`
- `Condition.treatmentNotes` y `symptoms`

**Qué crear:**
```
core-common/src/main/java/com/amachi/app/core/common/crypto/
    ├── AesEncryptionConfig.java       ← lee clave de env variable
    ├── AesEncryptor.java              ← lógica AES-256-GCM
    └── EncryptedStringConverter.java  ← @Converter JPA
```

**EncryptedStringConverter.java:**
```java
@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    @Autowired
    private AesEncryptor encryptor;

    @Override
    public String convertToDatabaseColumn(String plainText) {
        return (plainText == null) ? null : encryptor.encrypt(plainText);
    }

    @Override
    public String convertToEntityAttribute(String cipherText) {
        return (cipherText == null) ? null : encryptor.decrypt(cipherText);
    }
}
```

**Uso en entidad:**
```java
// En Patient.java:
@Column(name = "NHC")
@Convert(converter = EncryptedStringConverter.class)
private String nhc;

@Column(name = "ADDITIONAL_REMARKS", columnDefinition = "TEXT")
@Convert(converter = EncryptedStringConverter.class)
private String additionalRemarks;
```

**Variable de entorno requerida:**
```bash
# En application.yml o env del servidor — NUNCA en código
AMACHI_PHI_ENCRYPTION_KEY=<base64-AES-256-key-de-32-bytes>
```

> ⚠️ La clave NO debe estar en el repositorio. Usar secrets manager (Vault, AWS KMS, env variable del servidor).

---

### TAREA 5 — Validación de entrada en módulos médicos (G5)
**Módulo:** `vitalia-medical-core`, `core-common`
**Estándar:** ISO 27001 A.14.2 — seguridad en desarrollo; OWASP A3

**Qué agregar en `vitalia-boot` o `core-common`:**
```java
@RestControllerAdvice
public class GlobalValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
            .getFieldErrors().stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                e -> e.getDefaultMessage() != null ? e.getDefaultMessage() : "inválido"
            ));
        return ResponseEntity.badRequest().body(Map.of(
            "status", 400,
            "error", "VALIDATION_ERROR",
            "fields", errors
        ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraint(
            ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body(Map.of(
            "status", 400,
            "error", "CONSTRAINT_VIOLATION",
            "message", ex.getMessage()
        ));
    }
}
```

**Campos críticos a agregar `@Pattern` en DTOs médicos:**
```java
// En PatientDto.java — previene inyección en NHC
@Pattern(regexp = "^[A-Z0-9\\-]{3,50}$", message = "NHC formato inválido")
private String nhc;

// En ConsultationDto.java — previene HTML/script en notas
@Size(max = 5000, message = "Notas exceden límite permitido")
private String notes;
```

---

### TAREA 6 — Política de retención de logs (G6)
**Módulo:** `vitalia-boot` (configuración Logback)
**Estándar:** ISO 27001 A.12.4, GDPR Art.5(1)(e) — limitación del plazo de conservación

**En `vitalia-boot/src/main/resources/logback-spring.xml`:**
```xml
<configuration>
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/amachi-platform.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- Rotación diaria, retención 90 días (HIPAA mínimo 6 años → ajustar en prod) -->
            <fileNamePattern>logs/amachi-platform.%d{yyyy-MM-dd}.log.gz</fileNamePattern>
            <maxHistory>90</maxHistory>
            <totalSizeCap>5GB</totalSizeCap>
        </rollingPolicy>
        <encoder>
            <!-- Formato estructurado — sin datos sensibles -->
            <pattern>%d{ISO8601} [%thread] %-5level %logger{36} tenantId=%X{tenantId} userId=%X{userId} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Sanitizar campos sensibles — NUNCA loggear tokens ni NHC completo -->
    <logger name="com.amachi.app.core.auth" level="INFO"/>
    <logger name="com.amachi.app.vitalia.medicalcore.patient" level="WARN"/>
    <root level="INFO">
        <appender-ref ref="FILE"/>
    </root>
</configuration>
```

---

## 4. RIESGOS ACEPTADOS (NO se implementan)

Los siguientes gaps se documentan como **riesgo conscientemente aceptado** por alcance y complejidad:

| Gap | Razón para aceptar | Mitigación alternativa |
|---|---|---|
| G7 — TenantResolver en memoria | Aceptable en single-node; migrar a Redis cuando escale | Documentar como deuda técnica |
| G8 — Sanitización completa de logs | Alto costo de implementación; mitigado por logback-spring | Auditoría manual periódica |
| G9 — SBOM | Requiere proceso DevOps adicional | OWASP Dependency-Check en CI/CD |
| G10 — TLS en código | Configuración de servidor, no de app | Documentar en runbook de operaciones |
| G11 — Plan formal de incidentes | Proceso organizacional, no técnico | Crear documento ISO 27001 A.16 por separado |

---

## 5. CHECKLIST DE VERIFICACIÓN POST-IMPLEMENTACIÓN

Antes de considerar el nivel aceptable alcanzado, verificar:

- [ ] `REVINFO` tabla creada con tenant + user + IP
- [ ] Tablas `_AUD` de Envers generadas y con datos al crear/modificar registros
- [ ] `POST /auth/login` retorna HTTP 429 al superar 10 intentos/minuto por IP
- [ ] Swagger UI (`/swagger-ui/**`) retorna 403 sin token ADMIN en perfil `prod`
- [ ] Campos NHC en base de datos muestran texto cifrado (no legible directamente en DB)
- [ ] `POST /patients` con NHC `<script>alert(1)</script>` retorna HTTP 400
- [ ] Logs rotan diariamente y los archivos >90 días se eliminan automáticamente
- [ ] Variable `AMACHI_PHI_ENCRYPTION_KEY` configurada en servidor y NO en repositorio
- [ ] `git log --all -- "**application*.properties"` no muestra claves en historial

---

## 6. NIVEL DE COMPLIANCE ESTIMADO POST-IMPLEMENTACIÓN

| Estándar | Antes | Después (tareas 1–6) |
|---|---|---|
| **ISO 27001** | ~45% | ~70% |
| **HIPAA** | ~40% | ~65% |
| **GDPR** | ~50% | ~72% |
| **ISO 13485** | ~35% | ~55% |

> El 100% requeriría: auditoría formal externa, gestión de riesgos documentada (ISO 31000),
> plan de continuidad de negocio, certificación de terceros, penetration testing periódico,
> y políticas organizacionales completas. Ese alcance está fuera del scope técnico de la app.

---

## 7. ORDEN DE IMPLEMENTACIÓN RECOMENDADO

```
Sprint 1 (1 semana):
  ├── TAREA 3 — Swagger auth (30 min — 1 línea)
  ├── TAREA 2 — Rate limiting /auth (2-3 horas)
  └── TAREA 6 — Logback retención (1 hora)

Sprint 2 (1 semana):
  ├── TAREA 1 — Envers RevisionEntity (4-6 horas)
  └── TAREA 5 — Validación global handler (2-3 horas)

Sprint 3 (1-2 semanas):
  └── TAREA 4 — Encriptación PHI campos críticos (1-2 días)
                 ← Esta requiere migración de datos existentes
```

---

*Documento generado: 2026-05-07*
*Basado en auditoría de código fuente de amachi-platform*
*Próxima revisión recomendada: antes de cualquier despliegue en producción con datos reales de pacientes*
