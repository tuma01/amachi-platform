-- ============================================================
-- Script: V10_45__create_med_audit_tables_phase4.sql
-- Módulo: vitalia-medical-core
-- Descripción: Tablas de auditoría Hibernate Envers — Fase 4.
--              Entidades creadas en AMA-032 (V10_43, V10_44).
--              ISO 27001 A.12.4 — Trazabilidad de cambios clínicos.
-- Convención: ID + REV + REVTYPE + columnas base tenant-audit + cols propias
-- ============================================================

-- -------------------------------------------------------
-- MED_MEDICATION_DISPENSE_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_MEDICATION_DISPENSE_AUD (
    ID                      BIGINT          NOT NULL,
    REV                     BIGINT          NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    FK_ID_PATIENT           BIGINT,
    FK_ID_PRESCRIPTION      BIGINT,
    FK_ID_ENCOUNTER         BIGINT,
    FK_ID_DISPENSER         BIGINT,
    FK_ID_MEDICATION        BIGINT,
    STATUS                  VARCHAR(30),
    QUANTITY                DECIMAL(10,2),
    DAYS_SUPPLY             INT,
    LOT_NUMBER              VARCHAR(50),
    DISPENSED_AT            DATETIME(6),
    HANDED_OVER_AT          DATETIME(6),
    DISPENSER_NOTE          TEXT,
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_MED_DISP_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_MEDICATION_ADMINISTRATION_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_MEDICATION_ADMINISTRATION_AUD (
    ID                      BIGINT          NOT NULL,
    REV                     BIGINT          NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    FK_ID_PATIENT           BIGINT,
    FK_ID_PRESCRIPTION      BIGINT,
    FK_ID_DISPENSE          BIGINT,
    FK_ID_ENCOUNTER         BIGINT,
    FK_ID_NURSE             BIGINT,
    STATUS                  VARCHAR(30),
    ADMINISTERED_AT         DATETIME(6),
    DOSE_QUANTITY           DECIMAL(10,2),
    DOSE_UNIT               VARCHAR(30),
    ROUTE                   VARCHAR(50),
    NOTES                   TEXT,
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_MED_ADM_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
