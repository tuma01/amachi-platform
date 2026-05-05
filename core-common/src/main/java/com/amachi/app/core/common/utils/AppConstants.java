package com.amachi.app.core.common.utils;

import org.springframework.data.domain.Sort.Direction;
import java.time.Duration;

/**
 * Clase final que contiene todas las constantes de la aplicación organizadas
 * por categorías.
 * Se utiliza un patrón de anidamiento para agrupar constantes relacionadas.
 */
public final class AppConstants {

    private AppConstants() {
        throw new AssertionError("No se permite instanciar esta clase de constantes");
    }

    // Grupo para mensajes de error
    public static final class ErrorMessages {
        public static final String HEADER_MISSING = "has.msg.header.missing";
        public static final String HEADER_FORMAT_ERROR = "has.msg.header.format.error";
        public static final String PARAM_FORMAT_ERROR = "has.msg.format.error";
        public static final String ACCEPT_VERSION_NOT_SUPPORTED = "has.msg.header.version.supported";
        public static final String ACCEPT_NOT_SUPPORTED = "has.msg.header.version.format.supported";
        public static final String INTERNAL_SERVER_ERROR = "has.msg.internal.server.error";
        public static final String OBJECT_NOT_FOUND = "has.msg.object.not.found";
        public static final String UNHANDLED_ERROR = "has.msg.unhandler.error";

        // Mensajes específicos para integridad de datos
        public static final String DATA_INTEGRITY_GENERIC = "error.data.integrity.violation.generic";
        public static final String DATA_INTEGRITY_VALUE = "error.data.integrity.violation.value";
        public static final String ID_MUST_NOT_BE_NULL = "ID must not be null";
        public static final String TENANT_MUST_NOT_BE_NULL = "Tenant must not be null";
        public static final String ENTITY_MUST_NOT_BE_NULL = "Entity must not be null";
        public static final String DATA_MUST_NOT_BE_NULL = "Data must not be null";

        private ErrorMessages() {
        }
    }

    // Grupo para configuración de paginación
    public static final class Pagination {
        public static final String DEFAULT_PAGE_NUMBER = "0";
        public static final String DEFAULT_PAGE_SIZE = "10";
        public static final String DEFAULT_SORT_BY = "id";
        public static final Direction DEFAULT_SORT_DIRECTION = Direction.ASC;

        private Pagination() {
        }
    }

    // Grupo para roles de usuario
    public static final class UserRoles {
        public static final String PATIENT = "patient";
        public static final String DOCTOR = "doctor";
        public static final String NURSE = "nurse";
        public static final String ADMIN = "admin";
        public static final String SUPER_ADMIN = "super_admin";
        public static final String[] ALL_ROLES = { PATIENT, DOCTOR, NURSE, ADMIN, SUPER_ADMIN };

        private UserRoles() {
        }
    }

    // Grupo para patrones comunes
    public static final class Patterns {
        public static final String DUPLICATE_ENTRY_PREFIX = "Duplicate entry '";
        public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        public static final String PHONE_REGEX = "^\\+?[0-9]{10,15}$";

        private Patterns() {
        }
    }

    // Grupo para configuraciones de seguridad
    public static final class Security {
        public static final String JWT_SECRET = "mySecretKey";
        public static final long JWT_EXPIRATION_MS = 86400000; // 24 horas
        public static final String TOKEN_HEADER = "Authorization";
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final Duration RESET_TOKEN_VALIDITY = Duration.ofHours(24);

        private Security() {
        }
    }

    /**
     * Configuraciones relacionadas a recursos estáticos y archivos
     */
    public static final class Resources {
        /**
         * Ruta relativa del avatar por defecto dentro del classpath
         * Formato recomendado: prefijo 'classpath:' para recursos empaquetados
         */
        public static final String DEFAULT_AVATAR_PATH = "classpath:static/images/default-avatar.png";

        /**
         * Tipo MIME por defecto para avatares
         */
        public static final String DEFAULT_AVATAR_MIME_TYPE = "image/png";

        /**
         * Tamaño máximo en bytes para avatares (2MB)
         */
        public static final int MAX_AVATAR_SIZE_BYTES = 2 * 1024 * 1024;

        private Resources() {
        }
    }

    public static final class Roles {
        public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_TENANT_ADMIN = "ROLE_TENANT_ADMIN";
    }

    // Grupo para temas
    public static final class Themes {
        public static final String DEFAULT_THEME_CODE = "THEME_LIGHT_STD";

        private Themes() {
        }
    }

    public static final class Url {
        public static final String API_V1 = "/api/v1";

        private Url() {
        }
    }
}
