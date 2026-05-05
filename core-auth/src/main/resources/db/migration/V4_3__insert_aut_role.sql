 -- ***********************************************
 --  Insert AUT_ROLE — Roles del sistema (tenant_id = 0)
 --  SaaS Elite Tier 3: 0L = system-level data
 -- ***********************************************
INSERT INTO AUT_ROLE
    (ID, EXTERNAL_ID, VERSION, CREATED_BY, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_DATE,
     TENANT_ID, TENANT_CODE, IS_DELETED, NAME, DESCRIPTION)
VALUES
    (1,  UUID(), 0, 'SYSTEM', CURRENT_TIMESTAMP, NULL, NULL, 0, 'SYSTEM', 0, 'ROLE_SUPER_ADMIN',     'Super administrador con acceso total al sistema'),
    (2,  UUID(), 0, 'SYSTEM', CURRENT_TIMESTAMP, NULL, NULL, 0, 'SYSTEM', 0, 'ROLE_ADMIN',           'Administrador del tenant'),
    (3,  UUID(), 0, 'SYSTEM', CURRENT_TIMESTAMP, NULL, NULL, 0, 'SYSTEM', 0, 'ROLE_DOCTOR',          'Médico con acceso a expedientes clínicos'),
    (4,  UUID(), 0, 'SYSTEM', CURRENT_TIMESTAMP, NULL, NULL, 0, 'SYSTEM', 0, 'ROLE_NURSE',           'Enfermero/a con acceso clínico asistencial'),
    (5,  UUID(), 0, 'SYSTEM', CURRENT_TIMESTAMP, NULL, NULL, 0, 'SYSTEM', 0, 'ROLE_PATIENT',         'Paciente con acceso a su propio expediente'),
    (6,  UUID(), 0, 'SYSTEM', CURRENT_TIMESTAMP, NULL, NULL, 0, 'SYSTEM', 0, 'ROLE_EMPLOYEE',        'Empleado general del tenant'),
    (7,  UUID(), 0, 'SYSTEM', CURRENT_TIMESTAMP, NULL, NULL, 0, 'SYSTEM', 0, 'ROLE_USER',            'Usuario estándar del sistema'),
    (8,  UUID(), 0, 'SYSTEM', CURRENT_TIMESTAMP, NULL, NULL, 0, 'SYSTEM', 0, 'ROLE_GUEST',           'Invitado con acceso de solo lectura limitado'),
    (9,  UUID(), 0, 'SYSTEM', CURRENT_TIMESTAMP, NULL, NULL, 0, 'SYSTEM', 0, 'ROLE_MODERATOR',       'Moderador de contenido'),
    (10, UUID(), 0, 'SYSTEM', CURRENT_TIMESTAMP, NULL, NULL, 0, 'SYSTEM', 0, 'ROLE_CONTENT_CREATOR', 'Creador de contenido'),
    (11, UUID(), 0, 'SYSTEM', CURRENT_TIMESTAMP, NULL, NULL, 0, 'SYSTEM', 0, 'ROLE_CONTENT_EDITOR',  'Editor de contenido existente'),
    (12, UUID(), 0, 'SYSTEM', CURRENT_TIMESTAMP, NULL, NULL, 0, 'SYSTEM', 0, 'ROLE_SUPPORT',         'Personal de soporte técnico'),
    (13, UUID(), 0, 'SYSTEM', CURRENT_TIMESTAMP, NULL, NULL, 0, 'SYSTEM', 0, 'ROLE_ANALYST',         'Analista de datos');

COMMIT;
