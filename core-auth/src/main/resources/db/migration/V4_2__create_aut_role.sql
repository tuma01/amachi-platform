-- ================================================================
--  AUT_ROLE — SaaS Elite Tier 3
--  Herencia: BaseEntity → Auditable<String> → AuditableTenantEntity → Role
-- ================================================================
CREATE TABLE IF NOT EXISTS AUT_ROLE (

    -- [BaseEntity]
    ID              BIGINT          NOT NULL AUTO_INCREMENT,

    -- [Auditable<String>]
    EXTERNAL_ID     VARCHAR(36)     NULL,
    VERSION         BIGINT          NOT NULL DEFAULT 0,
    CREATED_BY      VARCHAR(100)    NOT NULL,
    CREATED_DATE    DATETIME(6)     NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100)    NULL,
    LAST_MODIFIED_DATE  DATETIME(6)     NULL,

    -- [AuditableTenantEntity]
    TENANT_ID       BIGINT          NOT NULL,
    TENANT_CODE     VARCHAR(50)     NOT NULL,
    IS_DELETED      TINYINT(1)      NOT NULL DEFAULT 0,

    -- [Role]
    NAME            VARCHAR(50)     NOT NULL,
    DESCRIPTION     VARCHAR(200)    NULL,

    -- Constraints
    CONSTRAINT PK_AUT_ROLE          PRIMARY KEY (ID),
    CONSTRAINT UK_ROLE_EXTERNAL_ID  UNIQUE      (EXTERNAL_ID),
    CONSTRAINT UK_ROLE_NAME         UNIQUE      (NAME)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

