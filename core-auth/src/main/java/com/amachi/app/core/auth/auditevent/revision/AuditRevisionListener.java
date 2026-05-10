package com.amachi.app.core.auth.auditevent.revision;

import com.amachi.app.core.auth.context.AuthContext;
import com.amachi.app.core.common.context.TenantContext;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Listener Envers que popula los campos extendidos de AuditRevisionEntity
 * en cada operación auditada.
 *
 * IMPORTANTE: esta clase NO puede usar @Autowired — Envers la instancia
 * fuera del contexto Spring. Se usan contextos estáticos (ThreadLocal):
 *   - SecurityContextHolder → AuthContext con userId y tenantCode
 *   - TenantContext         → fallback de tenantCode si no hay sesión
 */
public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        AuditRevisionEntity rev = (AuditRevisionEntity) revisionEntity;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()
                && auth.getPrincipal() instanceof AuthContext ctx) {
            rev.setUserId(ctx.getUserId());
            rev.setTenantCode(ctx.getTenantCode());
            rev.setUsername(auth.getName());
            return;
        }

        // Fallback: operaciones de sistema sin sesión de usuario (bootstrap, jobs)
        String tenantCode = TenantContext.getTenantCode();
        if (tenantCode != null) {
            rev.setTenantCode(tenantCode);
        }
        rev.setUsername("system");
    }
}
