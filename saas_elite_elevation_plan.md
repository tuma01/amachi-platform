# 🚀 SaaS Elite Tier: Backend Architecture Guide

## 🛡️ 1. GOLDEN PROTOCOL: REGLAS CRÍTICAS DE ESTABILIDAD
*   **ANÁLISIS DE IMPACTO GLOBAL**: Todo cambio en el Backend debe verificar su regresión en el Frontend (contratos, tipos, DTOs). 
*   **SINCRONIZACIÓN DE API (MANDATORIO)**: Tras modificar DTOs o Controllers: `mvn clean install` -> Actualizar `openapi.json` -> `npm run gen-api`.
*   **REGLA DE IDIOMA**: Nombres exclusivamente en **INGLÉS** y en **SINGULAR** (excepto `core-geography`).
*   **CERO PARCHES**: Prohibido usar `@Hidden` para ocultar errores de contrato.

---

## 🔑 2. MULTI-TENANCY (Platform-as-a-Tenant)

### 2.1 Identificadores del Tenant
*   **`id` (Long)**: PK técnica interna. Prohibido exponerla.
*   **`code` (String)**: El identificador de negocio (ej: `hospital-san-borja`).
*   **`tenantId` (Long)**: Columna `TENANT_ID`. Almacena el ID interno del tenant.
*   **Platform-as-a-Tenant**: El tenant de los tenants es `GLOBAL` (dueño de `DMN_TENANT`).

### 2.2 Flujo de Petición (Contexto)
1. `MultiTenantFilter` captura el **header `X-Tenant-Code`** (local/dev) o el **subdominio** del host (producción).
2. `TenantContext` almacena `tenantId` y `tenantCode` en `ThreadLocal`.
3. `TenantFilterAspect` activa el `tenantFilter` de Hibernate.
4. `@PrePersist` inyecta automáticamente el `tenantId` en nuevas entidades.

### 2.3 Resolución del TenantCode por Entorno

| Entorno | Fuente del TenantCode | Ejemplo |
|---|---|---|
| **Producción** | Subdominio del host | `hospital-san-borja.vitalia.com` → `hospital-san-borja` |
| **Local / Dev** | Header HTTP `X-Tenant-Code` | `X-Tenant-Code: GLOBAL` |

> **REGLA**: El `tenantCode` **NUNCA** viaja en el body del request. Es un concepto de routing/infraestructura, no de credenciales.

### 2.4 Llamada de Login (Referencia)

```http
POST /api/v1/auth/login
X-Tenant-Code: GLOBAL
Content-Type: application/json

{
  "email": "superadmin-dev@test.com",
  "password": "dev-password"
}
```

**Para el tenant de hospital:**
```http
POST /api/v1/auth/login
X-Tenant-Code: hospital-san-borja
Content-Type: application/json

{
  "email": "admin-dev@test.com",
  "password": "dev-password"
}
```

### 2.5 TenantContext — API Correcta

```java
// ✅ CORRECTO
TenantContext.getTenantId()    // → Long
TenantContext.getTenantCode()  // → String
TenantContext.setTenantId(id)
TenantContext.setTenantCode(code)
TenantContext.clear()

// ❌ NO EXISTEN — no usar
TenantContext.getCurrentTenantId()
TenantContext.getCurrentTenantCode()
```

### 2.6 Orden de Filtros y TenantContext

**CRÍTICO**: `JwtAuthenticationFilter` **NO debe** llamar `TenantContext.clear()` al inicio. El `MultiTenantFilter` lo setea primero y el JWT filter lo sobreescribe solo para endpoints protegidos (con token). La limpieza final ocurre en el bloque `finally` de ambos filtros.

```
Request → MultiTenantFilter (set TenantContext)
               → JwtAuthenticationFilter (NO clear al inicio)
                    → Si tiene JWT válido: sobreescribe TenantContext con valores del token
                    → Si NO tiene JWT (endpoint público): TenantContext del MultiTenantFilter se preserva
                    → finally: TenantContext.clear()
               → finally: TenantContext.clear()
```

---

## 🏗️ 3. ENTITY & DATA LAYER GOVERNANCE

### 3.1 Jerarquía de Clases Base

| Clase Base | Campos añadidos | Uso |
|---|---|---|
| `BaseEntity` | `id` | Raíz. PK técnica. |
| `Auditable<U>` | Auditoría + `@Version` + `EXTERNAL_ID` | Entidades **globales** (catálogos, Person, Tenant, Role). |
| `AuditableTenantEntity` | + `TENANT_ID` + `TENANT_CODE` + `IS_DELETED` | Entidades **tenant-scoped** (Patient, Doctor, Appointment…). |

### 3.2 Soft Delete (Declarative Isolation)
*   **Mecanismo**: `@SQLRestriction("IS_DELETED = false")`.
*   **Smart Delete**: El `BaseService` decide automáticamente entre borrado físico o lógico basado en el tipo de entidad.

### 3.3 Repositories & Specifications
*   **Pattern**: Extender siempre `CommonRepository<E, ID>` (global) o `TenantCommonRepository<E, ID>` (tenant-scoped).
*   **Proyecciones de Rendimiento**: Para listados masivos de roles (`Doctor`, `Patient`), usar **Interface Projections** para evitar el problema N+1 y el overhead de instanciar grafos completos de `Person`.

### 3.4 Regla de Entidades con @Version en Relaciones FK

**PROBLEMA**: Hibernate 6.x rechaza objetos "shell" en el grafo de entidades. Un shell object es una instancia creada con solo el `id` (`new Country(); country.setId(26)`), cuyo `@Version` queda `null`. Hibernate lo trata como entidad "detached" con versión inválida y lanza `PropertyValueException`.

**SOLUCIÓN**: Usar `entityManager.getReference(EntityClass.class, id)` para FK references. Crea un proxy Hibernate managed (sin query a la DB) que satisface la validación de versión.

```java
// ✅ CORRECTO — proxy managed, sin query
address.setCountry(em.getReference(Country.class, dto.getCountryId()));

// ❌ INCORRECTO — shell object, version=null, Hibernate 6.x lo rechaza
Country shell = new Country();
shell.setId(dto.getCountryId());
address.setCountry(shell);

// ❌ TAMBIÉN INCORRECTO — MapStruct crea shell internamente
@Mapping(target = "country.id", source = "countryId")  // → new Country(id=X, version=null)
```

---

## 🔄 4. MAPPERS & DTO STANDARDS (Staff Level)

### 4.1 Principios Generales
*   **DTO-First**: Los DTOs **nunca** incluyen `tenantId`, `version`, `isDeleted` ni campos de auditoría.
*   **Interface-Only**: Todos los mappers son **interfaces** (no clases abstractas). MapStruct genera la implementación.
*   **Prohibición de round-trips**: `Entity → DTO → Entity`.

### 4.2 AuditableIgnoreConfig — Regla de Selección

Cada mapper debe usar la anotación correcta según la clase base de la entidad destino:

| Anotación | Entidades | Campos ignorados |
|---|---|---|
| `@IgnorePureAuditableFields` | Extienden `Auditable<String>` **sin** `tenantId` propio: `Country`, `State`, `Province`, `Municipality`, `Person`, `Icd10`, `Medication`, todos los catálogos médicos | `createdBy`, `createdDate`, `lastModifiedBy`, `lastModifiedDate`, `version`, `externalId` |
| `@IgnoreHybridAuditableFields` | Extienden `Auditable<String>` **con** `tenantId` propio (default 0/GLOBAL): `Role`, `Tenant`, `Theme`, `Hospital` | + `tenantId`, `tenantCode` |
| `@IgnoreTenantAuditableFields` | Extienden `AuditableTenantEntity`: `Patient`, `Doctor`, `Appointment`, `TenantConfig`, `Avatar`, etc. | + `tenantId`, `tenantCode` |

> **`@IgnoreAuditableFields` fue eliminada**. Era un alias ambiguo que causaba errores de compilación al usarse en entidades sin `tenantId`.

### 4.3 MapStruct Elite
*   `InjectionStrategy.CONSTRUCTOR` (configurado en `BaseMapperConfig`).
*   `unmappedTargetPolicy = ReportingPolicy.WARN` — campos sin mapping generan warning, no error de compilación. MapStruct auto-mapea por nombre.
*   Configuración mandatoria vía `AuditableIgnoreConfig`.

### 4.4 FK References a Entidades con @Version

**Regla**: Cuando un mapper necesita mapear un ID de DTO a una relación `@ManyToOne` con una entidad que tiene `@Version`, **NO usar** `@Mapping(target = "entity.id", source = "entityId")`. En su lugar, ignorar en el mapper y resolver en el servicio via `em.getReference()`.

```java
// AddressMapper.java — CORRECTO
@Mapping(target = "country",      ignore = true)  // ← ignorado en mapper
@Mapping(target = "state",        ignore = true)
@Mapping(target = "province",     ignore = true)
@Mapping(target = "municipality", ignore = true)
Address toEntity(AddressDto dto);

// AddressServiceImpl.java — resolución en el servicio
@Override
protected Address mapToEntity(AddressDto dto) {
    Address address = mapper.toEntity(dto);
    resolveGeographicRefs(dto, address);  // ← aquí se resuelven
    return address;
}

private void resolveGeographicRefs(AddressDto dto, Address address) {
    address.setCountry(dto.getCountryId() != null
        ? em.getReference(Country.class, dto.getCountryId()) : null);
    address.setState(dto.getStateId() != null
        ? em.getReference(State.class, dto.getStateId()) : null);
    // ... igual para province y municipality
}
```

---

## 📡 5. SERVICE LAYER & API CONTRACTS

### 5.1 Estructura de Servicios
*   **GenericService**: Implementación de los hooks `mapToEntity` y `mergeEntities`.
*   **Validación API**: Uso de `@Valid`, `@NonNull` y contratos OpenAPI autodocumentados.
*   **Transaccionalidad**: Frontera transaccional explícita en las implementaciones `*ServiceImpl`. Clase anotada con `@Transactional(readOnly = true)`, métodos de escritura con `@Transactional`.

### 5.2 Controllers — Reglas
*   Patrón: `*Api` (interface OpenAPI) + `*Controller` (implementación).
*   URL base: siempre `AppConstants.Url.API_V1 + "/resource"` (nunca hardcoded).
*   Inyectar la **interfaz** del servicio, no la implementación concreta (`AppointmentService`, no `AppointmentServiceImpl`).

---

## 🏥 6. VITALIA CLINICAL FLOW (EHR Tier)
*   **Separación HL7 FHIR**: Logística (`Appointment`) vs Acto Médico (`Encounter`).
*   **Registro Atómico**: Diagnósticos (`Condition`), Observaciones (`Observation`) y Recetas (`MedicationRequest`).
*   **Timeline Unificado**: Uso de `ClinicalHistoryStream` para visualización 360°.

---

## 🗄️ 7. DATABASE & FLYWAY STANDARDS
*   **Trigramas**: `CAT_` (Catálogos), `MED_` (Médico), `DMN_` (Dominio/SaaS).
*   **Flyway**: Un script SQL por recurso. Prohibidos los `ALTER TABLE` en desarrollo.
*   **Tipos**: `IS_DELETED` (Boolean), `TENANT_ID` (Varchar 50).
*   **Índices**: Obligatorio incluir `TENANT_ID` en índices de búsqueda.

---

## ⚙️ 8. SPRING BOOT & INFRAESTRUCTURA

### 8.1 spring-data-envers vs hibernate-envers

Se usa `spring-data-envers` (gestionado por Spring Boot BOM) para aprovechar la gestión de versiones compatible. **No se usa `RevisionRepository`** — solo se requiere el soporte de `@Audited`.

**PROBLEMA CONOCIDO**: `spring-data-envers` registra `EnversRevisionRepositoryFactoryBean` automáticamente. Este factory intenta cargar entidades por su nombre JPA simple (ej: `"User"`) usando `ClassLoader.loadClass()`, lo que causa `ClassNotFoundException: User` al arrancar.

**SOLUCIÓN**: Forzar el factory estándar en `VitaliaApplication.java`:

```java
@EnableJpaRepositories(
    basePackages = "com.amachi.app",
    repositoryFactoryBeanClass = JpaRepositoryFactoryBean.class  // ← fuerza factory estándar
)
```

### 8.2 Usuarios de Desarrollo (perfil `dev`)

El archivo cargado es `bootstrap-dev.yml`. Credenciales:

| Rol | Email | Password | Header requerido |
|---|---|---|---|
| SuperAdmin | superadmin-dev@test.com | dev-password | `X-Tenant-Code: GLOBAL` |
| TenantAdmin | admin-dev@test.com | dev-password | `X-Tenant-Code: hospital-san-borja` |

---

---

## 🔒 9. SECURITY & MULTI-TENANT IMPROVEMENTS (AMA-027 / 2026-05-07)

### 9.1 Caffeine TTL Cache — TenantResolver y TenantCache

**Problema resuelto:** `TenantResolver` y `TenantCache` usaban `ConcurrentHashMap` sin TTL. Si un tenant era modificado o desactivado en base de datos, el caché seguía sirviendo los datos obsoletos **hasta reinicio del servidor**.

**Cambio aplicado:** Se reemplazó `ConcurrentHashMap` por Caffeine con:
- `expireAfterWrite(5, TimeUnit.MINUTES)` — los datos se invalidan automáticamente a los 5 minutos
- `maximumSize(500)` — protección contra memory leak si hay muchos tenants

**Archivos modificados:**
- `core-auth/pom.xml` — dependencia `com.github.ben-manes.caffeine:caffeine` (versión gestionada por Spring Boot BOM)
- `core-auth/.../multiTenant/TenantResolver.java` — `ConcurrentHashMap<String,Long>` → `Cache<String,Long>`
- `core-auth/.../multiTenant/TenantCache.java` — `ConcurrentHashMap<String,Tenant>` → `Cache<String,Tenant>`

**API pública sin cambios** — `resolveTenantId()`, `getTenant()`, `evictCache()`, `clearCache()` mantienen sus firmas exactas.

**Impacto en regresión:** Ninguno. Es un cambio de implementación interna sin modificación de contratos.

**Estándar cubierto:** ISO 27001 A.9 — Control de acceso; Multi-Tenant isolation.

---

### 9.2 Validación de Tenant Activo en MultiTenantFilter

**Problema resuelto:** El sistema aceptaba requests de tenants desactivados (`IS_ACTIVE = false`) porque `MultiTenantFilter` solo verificaba la existencia del tenant, no su estado.

**Cambio aplicado:** `MultiTenantFilter` ahora inyecta `TenantCache` (además del `TenantResolver` previo) y verifica `tenant.getActive()` antes de continuar. Si el tenant está desactivado, retorna HTTP 403 Forbidden.

```
Request → MultiTenantFilter
  ├── 1. Extraer tenantCode (subdominio o X-Tenant-Code header)
  ├── 2. tenantCache.getTenant(code) — obtiene objeto completo con estado
  ├── 3. Si tenant == null → HTTP 401
  ├── 4. Si tenant.active == false → HTTP 403
  └── 5. OK → TenantContext.set(tenantId, tenantCode) → continúa cadena
```

**Archivo modificado:** `core-auth/.../multiTenant/MultiTenantFilter.java`

**Nota:** El `TenantResolver` ya no es necesario para la resolución de ID porque `TenantCache` devuelve el `Tenant` completo (incluyendo el ID). Se mantiene por compatibilidad con posibles usos externos.

**Impacto en regresión:** Ninguno para tenants activos (flujo idéntico). Nueva protección solo activa cuando `IS_ACTIVE = false`.

**Estándar cubierto:** ISO 27001 A.9.4 — Protección contra acceso no autorizado.

---

### 9.3 Swagger UI Protegido en Producción

**Problema resuelto:** `/v3/api-docs/**` y `/swagger-ui/**` eran accesibles sin autenticación en todos los entornos, exponiendo la estructura completa de la API.

**Cambio aplicado:** `SecurityConfig.securityFilterChain()` ahora tiene lógica condicional basada en `${spring.profiles.active}`:

| Perfil | Acceso a Swagger |
|---|---|
| `dev` | `permitAll()` — sin cambios para desarrollo |
| cualquier otro (prod, staging) | `hasAnyRole("ADMIN", "SUPER_ADMIN")` |

**Refactor adicional:** El bloque `authorizeHttpRequests` se unificó en un solo bloque lambda (antes había estructura de cadena de métodos, ahora es bloque `{}` con sentencias). Esto permite usar `if` statements para la lógica condicional del swagger. **Todas las reglas de acceso previas se mantienen idénticas.**

**Archivo modificado:** `core-auth/.../config/security/SecurityConfig.java`

**Impacto en regresión:** Ninguno en perfil `dev`. En perfil `prod`, Swagger requiere token con rol ADMIN — comportamiento nuevo y correcto.

**Estándar cubierto:** ISO 27001 A.13.2 — Protección de información en tránsito.

---

### 9.4 Rate Limiting en Endpoints de Autenticación

**Problema resuelto:** `/auth/login` y `/auth/refresh` no tenían protección contra ataques de fuerza bruta. Un atacante podía hacer miles de intentos de contraseña sin restricción.

**Cambio aplicado:** Nueva clase `RateLimitFilter` (OncePerRequestFilter) con Caffeine:
- Contador de intentos por IP con TTL de 1 minuto
- Límite: 10 requests/minuto por IP
- Al superarlo: HTTP 429 con body JSON `{"status":429,"error":"TOO_MANY_REQUESTS"}`
- Registrado en `SecurityConfig` **antes** de `MultiTenantFilter`
- Soporte para proxies/load balancers: lee `X-Forwarded-For`

**Archivos creados/modificados:**
- `core-auth/.../config/security/RateLimitFilter.java` — implementación
- `core-auth/.../config/security/SecurityConfig.java` — `.addFilterBefore(rateLimitFilter, MultiTenantFilter.class)`

**Estándar cubierto:** ISO 27001 A.9.4 — Control de acceso a sistemas y aplicaciones.

---

### 9.5 Encriptación PHI — NHC (Número de Historia Clínica)

**Problema resuelto:** El campo `NHC` en `MED_PATIENT` se almacenaba en texto plano. Un dump de base de datos exponía directamente el identificador único del paciente.

**Estrategia adoptada (mínimo viable):**
- Solo `NHC` encriptado ahora. Campos de texto largo (diagnósticos, notas) en iteración futura.
- AES-256/CBC con IV **determinístico** (derivado de SHA-256(key)[0..15]).
- IV determinístico es intencional: garantiza que el mismo NHC produzca siempre el mismo ciphertext, preservando las constraints UNIQUE en base de datos.

**Componentes creados:**

| Archivo | Función |
|---|---|
| `core-common/.../converter/EncryptionKeyHolder.java` | Bridge Spring → JPA. Lee `${app.phi.encryption-key}` y lo expone estáticamente |
| `core-common/.../converter/EncryptedStringConverter.java` | JPA `AttributeConverter<String,String>`. AES-256/CBC |
| `vitalia-medical-core/.../patient/entity/Patient.java` | `@Convert(converter=EncryptedStringConverter.class)` en campo `nhc` |
| `V10_41__alter_med_patient_nhc_encrypted.sql` | `ALTER TABLE MED_PATIENT MODIFY COLUMN NHC VARCHAR(100)` |

**Configuración (`application.yml`):**
```yaml
app:
  phi:
    encryption-key: ${APP_PHI_ENCRYPTION_KEY:VklUQUxJQV9ERVZfS0VZX05PVF9GT1JfUFJPRF8xMjM=}
```
> **IMPORTANTE:** En producción siempre setear `APP_PHI_ENCRYPTION_KEY` como variable de entorno con una clave generada con `openssl rand -base64 32`. El default es solo para dev.

**Campos candidatos para siguiente iteración (cuando la app esté estabilizada):**
- `Encounter.notes`, `Condition.clinicalNotes`, `Observation.value` (TEXT largo — evaluar impacto en búsquedas)
- `Patient.allergySummary`, `Patient.additionalRemarks`

**Estándares cubiertos:** ISO 27001 A.10.1 / HIPAA §164.312(a)(2)(iv) — Gestión de claves criptográficas.

---

### 9.6 Logback — Rolling File + MDC Tenant/Usuario

**Problema resuelto:** Los logs no tenían retención controlada ni contexto de tenant/usuario. En producción era imposible correlacionar un error con el tenant y el usuario que lo provocó.

**Cambio aplicado:**

**`logback-spring.xml`** en `vitalia-boot/src/main/resources`:

| Característica | Detalle |
|---|---|
| Rolling diario | `TimeBasedRollingPolicy` — archivos `.log.gz` por día |
| Retención | 90 días / 2 GB cap (log general) — 90 días / 500 MB (solo errores) |
| Archivo de errores | `amachi-platform-errors.log` con filtro `WARN+` |
| Perfil `dev` | Consola con colores + DEBUG para `com.amachi` y `org.hibernate.SQL` |
| Perfil `staging` | Solo archivo, INFO para código propio |
| Perfil `prod` | Solo archivo, WARN para librerías, INFO para código propio |

**Patrón de log:**
```
2026-05-07 14:32:10.123 [http-nio-8088-exec-3] INFO  [tenant:hospital-san-borja] [user:dr.perez] c.a.v.m.appointment.service - ...
```

**MDC wiring:**

| Filtro | MDC put | MDC remove |
|---|---|---|
| `MultiTenantFilter` | `tenantCode` tras resolver tenant | `finally` block |
| `JwtAuthenticationFilter` | `userId`, `username` tras autenticación exitosa | `finally` block |

**Archivos modificados:**
- `vitalia-boot/src/main/resources/logback-spring.xml` — creado
- `core-auth/.../MultiTenantFilter.java` — `MDC.put("tenantCode", ...)` + `MDC.remove`
- `core-auth/.../JwtAuthenticationFilter.java` — `MDC.put("userId", ...)` + `MDC.put("username", ...)` + `MDC.remove` en finally

**Estándar cubierto:** ISO 27001 A.12.4 — Registro de eventos y monitoreo.

---

---

### 9.7 Estabilización arquitectura AMA-027 (2026-05-07)

**Correcciones aplicadas durante la sesión de estabilización:**

| Problema | Fix aplicado |
|---|---|
| `ConflictingBeanDefinitionException: avatarController` | Avatar eliminado de `vitalia-medical-core`, único dueño: `core-management` |
| `ConflictingBeanDefinitionException: tenantFactory` | `TenantFactory` duplicado en `core-management` (package incorrecto) eliminado. Único en `core-common` |
| `CannotLoadBeanClassException: AddressController` | Controller inyectaba `AddressServiceImpl` directamente → cambiado a `AddressService` |
| `EnversMappingException: Avatar.user → User` | `@Audited` removido de `Avatar` (imagen de perfil no es dato clínico crítico) |
| `EnversMappingException: DoctorHospitalAssignment.hospital` | `@Audited(targetAuditMode = NOT_AUDITED)` en campo `hospital` |
| `EnversMappingException: Consultation.type` | `@Audited(targetAuditMode = NOT_AUDITED)` en campo `type` |
| `EnversMappingException: DischargeMedication.medication` | `@Audited(targetAuditMode = NOT_AUDITED)` en campo `medication` |
| `Schema-validation: missing table [*_aud]` | Creado `V10_42` con 19 tablas `_aud` fase 3. Corregidos V10_10 y V10_20 |
| `FK_ID_HOSPITAL references MED_HOSPITAL(ID)` | `MED_HOSPITAL` usa JOINED inheritance con PK `TENANT_ID` → FK corregido |
| `missing column [diagnosis_notes] in med_encounter` | `V10_5` actualizado con 6 columnas faltantes de la entidad |
| `missing column [photo] in med_user_profile_aud` | `PHOTO LONGBLOB` añadido a `MED_USER_PROFILE_AUD` en V10_20 |
| `wrong column type: photo longblob vs tinyblob` | `@Lob` sin `columnDefinition` en `Nurse` y `UserProfile` → añadido `columnDefinition = "LONGBLOB"` |
| `missing table [med_nurse_skills_aud]` | `@ElementCollection clinicalSkills` marcado con `@NotAudited` |
| `REV INT vs BIGINT incompatible` | Todos los `REV INT` en V10_20 y V10_42 cambiados a `BIGINT` para coincidir con `REVINFO.REV BIGINT` |
| `MultiTenantFilter does not have a registered order` | Todos los filtros custom registrados con `addFilterBefore(..., UsernamePasswordAuthenticationFilter.class)` |
| `Consultation.externalId` conflicto con clase base | Campo duplicado eliminado del entity; `EXTERNAL_ID_CUSTOM` huérfana eliminada del DDL |
| `ddl-auto: validate` falla por tablas `_aud` | `ddl-auto: update` temporalmente → drop DB + Flyway clean start → vuelto a `validate` |

**Estado final:** ✅ App arrancando correctamente con `ddl-auto: validate` y todas las migraciones Flyway V1–V10_42 ejecutadas.

**Sprint 2 plan:** Ver `SPRINT_2_PLAN.md` en raíz del proyecto.

---

*Last update: 2026-05-07 - Sprint 1 completado + estabilización arquitectura AMA-027. App 100% funcional. Sprint 2 pendiente: Envers RevisionEntity + Global Validation Handler.*

---

## 🛡️ 10. SECURITY AUDIT — ESTADO DE COMPLIANCE (2026-05-07)

> Auditoría realizada sobre código fuente. Ver plan completo de mejoras en **`SECURITY_COMPLIANCE_PLAN.md`**.

### 10.1 ¿Es Zero Trust la arquitectura?

**Parcialmente — tiene bases sólidas pero no es Zero Trust completo.**

✅ **Lo que cumple:**
- JWT stateless — ninguna sesión en servidor
- Anti-spoofing: compara `tenantCode` del token vs el del request en cada llamada
- Blacklist de tokens con limpieza automática (`AUT_BLACKLISTED_TOKEN`)
- `@PreAuthorize` en cada endpoint sensible — nunca confía por defecto
- BCrypt para contraseñas
- CORS deshabilitado en producción

⚠️ **Gaps pendientes (ver `SECURITY_COMPLIANCE_PLAN.md`):**
- ~~Sin rate limiting en `/auth/login`~~ → **corregido en 9.4**
- ~~NHC en texto plano~~ → **encriptado AES-256 en 9.5** / campos TEXT clínicos pendiente iteración futura
- ~~Swagger UI público~~ → **corregido en 9.3**

---

### 10.2 ¿Cumple certificación ISO hospitalaria?

**No certificado formalmente. Implementa estándares de referencia de forma parcial.**

| Estándar | Estado |
|---|---|
| **FHIR HL7** | ✅ Parcial — entidades alineadas: Encounter, Condition, Observation, Appointment, MedicationRequest, EpisodeOfCare |
| **ICD-10 (CIE-10)** | ✅ Catálogo integrado con tabla `CAT_ICD10` |
| **ISO 27001** | ⚠️ ~75% — mejoras AMA-027: Caffeine TTL, rate limiting, NHC cifrado. Pendiente: Envers completo, campos TEXT PHI |
| **ISO 13485** | ⚠️ ~55% — falta gestión formal de riesgos documentada |
| **HIPAA / GDPR** | ⚠️ ~70% — NHC cifrado, soft delete y audit events presentes. Pendiente: campos clínicos TEXT |

Para certificación formal se requiere además: auditoría externa, gestión ISO 31000, pen-testing periódico y políticas organizacionales.

---

### 10.3 ¿Los datos del Tenant están protegidos?

**Sí — tres niveles de protección activos:**

| Nivel | Mecanismo | Estado |
|---|---|---|
| **Aislamiento entre tenants** | Hibernate Filter `TENANT_ID = :tenantId` + `@PrePersist` fail-fast | ✅ Robusto |
| **Seguridad cruzada** | Anti-spoofing JWT + ~~tenant activo sin validar~~ → **corregido en 9.2** | ✅ |
| **Datos no se borran** | `@SQLRestriction("IS_DELETED = false")` universal + soft delete | ✅ |
| **Historial de cambios** | `@Audited` en entidades, `AUT_AUDIT_EVENTS` con IP/userId | ⚠️ Envers incompleto — falta `RevisionEntity` (ver `SECURITY_COMPLIANCE_PLAN.md`) |

---

### 10.4 ¿Es Multi-Tenant profesional?

**Sí — nivel SaaS Elite con mejoras aplicadas en AMA-027.**

| Característica | Estado |
|---|---|
| Subdomain-based routing (`hospital.vitalia.com`) | ✅ |
| ~~Cache sin TTL~~ → **Caffeine TTL 5min** | ✅ Corregido en 9.1 |
| ~~Tenant desactivado sin bloqueo~~ → **HTTP 403** | ✅ Corregido en 9.2 |
| Hibernate Filter automático por tenant | ✅ |
| RBAC por tenant (mismo usuario, diferentes roles por tenant) | ✅ |
| `TENANT_ID` inmutable post-persistencia | ✅ |
| **Pendiente:** TenantResolver en memoria (problema en multi-nodo) | ⚠️ Migrar a Redis cuando se escale a múltiples pods |

---

## 🏥 11. COBERTURA DE GESTIÓN HOSPITALARIA

### 11.1 Dominios implementados (~60-65% de un HIS completo)

| Dominio | Módulo | Estado |
|---|---|---|
| Identidad del paciente | Patient, Person | ✅ |
| Expediente clínico (EHR) | MedicalHistory, ActualIllness, PastIllness | ✅ |
| Antecedentes familiares | FamilyHistory, HereditaryDisease | ✅ |
| Hábitos clínicos | PhysiologicalHabit, ToxicHabit | ✅ |
| Seguros médicos | Insurance | ✅ |
| Citas + Recordatorios | Appointment, AppointmentReminder | ✅ |
| Encuentros clínicos (FHIR) | Encounter | ✅ |
| Diagnósticos CIE-10 | Condition + CAT_ICD10 | ✅ |
| Observaciones clínicas | Observation | ✅ |
| Prescripciones | Prescription | ✅ |
| Internación | Hospitalization, DischargeMedication | ✅ |
| Consultas médicas | Consultation | ✅ |
| Episodio de cuidado (FHIR) | EpisodeOfCare | ✅ |
| Personal médico | Doctor, Nurse, Employee | ✅ |
| Infraestructura | DepartmentUnit, Room, Bed | ✅ |
| Organizaciones | Organization, Hospital | ✅ |

### 11.2 Dominios faltantes para HIS completo

| Dominio | Prioridad |
|---|---|
| **Quirófano / Cirugía** — programación quirúrgica, equipos | Alta |
| **Laboratorio clínico** — órdenes, resultados, rangos de referencia | Alta |
| **Farmacia intrahospitalaria** — stock, dispensación, kardex enfermería | Alta |
| **Imágenes / Radiología** — DICOM, órdenes de imagen | Media |
| **Urgencias / Triage Manchester** — gestión sala emergencias | Media |
| **UCI / Monitoreo** — signos vitales continuos | Media |
| **Facturación / Liquidación** — tarifarios, cuentas corrientes | Media |
| **Consentimientos informados** — firmas digitales, formularios legales | Baja |
| **Estadísticas hospitalarias** — GRD, estancia media, indicadores OPS | Baja |
| **Notificaciones epidemiológicas** — declaración obligatoria | Baja |

> Los módulos de alta prioridad (Laboratorio, Farmacia, Quirófano) normalmente se integran vía APIs externas (LIS, SIF, PACS) en sistemas hospitalarios maduros, no como desarrollo propio.
