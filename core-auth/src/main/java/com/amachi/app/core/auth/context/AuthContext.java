package com.amachi.app.core.auth.context;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Contexto de autenticación unificado (SaaS Ready).
 * Este objeto actúa como el principal en el SecurityContext de Spring.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthContext {
    private Long userId;
    private Long tenantId;
    private String tenantCode;
    private Set<String> roles;
}
