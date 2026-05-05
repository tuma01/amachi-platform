-- ============================================================
-- Script: V10_1__create_med_hospital.sql
-- Módulo: vitalia-medical
-- Tabla: MED_HOSPITAL (JOINED Inheritance desde DMN_TENANT)
-- Descripción: Tabla hija del Tenant para perfiles médicos de Hospital.
--
-- ARQUITECTURA:
--   - Hospital extends Tenant (JOINED): TENANT_ID es PK y FK a DMN_TENANT.
--   - Los campos base (code, name, auditoría, tenant isolation) viven en DMN_TENANT.
--   - Aquí solo van los campos específicos del Hospital.
--   - FK_ID_PERSON: referencia a la identidad legal del hospital (Golden Protocol).
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_HOSPITAL (

    -- PK y FK hacia la tabla base (JOINED Inheritance)
    TENANT_ID               BIGINT PRIMARY KEY,

    -- Identidad Legal (Composición - Golden Protocol)
    FK_ID_PERSON            BIGINT,

    -- Datos Médico-Legales
    LEGAL_NAME              VARCHAR(200) NOT NULL,
    TAX_ID                  VARCHAR(50)  NOT NULL,
    MEDICAL_LICENSE         VARCHAR(100) NOT NULL,

    -- Contacto Operativo
    WEBSITE                 VARCHAR(255),
    CONTACT_PHONE           VARCHAR(50),
    CONTACT_EMAIL           VARCHAR(100),

    -- Datos Institucionales (Fase 13)
    MEDICAL_DIRECTOR        VARCHAR(150),
    MEDICAL_DIRECTOR_LICENSE VARCHAR(100),
    HOSPITAL_CATEGORY       VARCHAR(100),
    BED_CAPACITY            INT,
    OPERATING_ROOMS_COUNT   INT,
    EMERGENCY_247           TINYINT(1) DEFAULT 0,
    SLOGAN                  VARCHAR(255),
    FAX_NUMBER              VARCHAR(50),
    WHATSAPP_NUMBER         VARCHAR(50),
    SOCIAL_LINKS            TEXT,
    SEAL_URL                VARCHAR(255),

    -- Constraints & Indexes
    CONSTRAINT FK_HOSPITAL_TENANT
        FOREIGN KEY (TENANT_ID) REFERENCES DMN_TENANT(ID) ON DELETE CASCADE,

    CONSTRAINT FK_HOSPITAL_PERSON
        FOREIGN KEY (FK_ID_PERSON) REFERENCES DMN_PERSON(ID),

    INDEX IDX_HOSPITAL_TAX_ID  (TAX_ID),
    INDEX IDX_HOSPITAL_PERSON  (FK_ID_PERSON)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
