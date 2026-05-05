package com.amachi.app.core.auth.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Acceso estático al contexto de autenticación unificado.
 */
public class AuthContextHolder {

    private AuthContextHolder() {}

    /**
     * Obtiene el contexto de autenticación actual.
     * @return AuthContext o null si no hay autenticación.
     */
    public static AuthContext get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthContext) {
            return (AuthContext) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * Obtiene el ID del usuario actual.
     */
    public static Long getUserId() {
        AuthContext ctx = get();
        return ctx != null ? ctx.getUserId() : null;
    }

    /**
     * Obtiene el ID del tenant actual.
     */
    public static Long getTenantId() {
        AuthContext ctx = get();
        return ctx != null ? ctx.getTenantId() : null;
    }

    /**
     * Obtiene el código del tenant actual.
     */
    public static String getTenantCode() {
        AuthContext ctx = get();
        return ctx != null ? ctx.getTenantCode() : null;
    }

    /**
     * Verifica si el usuario actual es Super Administrador.
     */
    public static boolean isSuperAdmin() {
        AuthContext ctx = get();
        return ctx != null && ctx.getRoles() != null && 
               ctx.getRoles().contains("ROLE_SUPER_ADMIN");
    }
}
