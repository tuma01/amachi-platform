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

*Last update: 2026-05-05 - AMA-026: SaaS Elite elevation, startup fixes, mapper patterns, tenant flow.*
