package com.amachi.app.core.common.aspect;

import com.amachi.app.core.common.context.TenantContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

/**
 * Aspecto de Seguridad para Aislamiento Multi-Tenant (SaaS Elite Hardened).
 * Actúa como validador y activador del filtro de Hibernate.
 */
@Aspect
@Component
public class TenantFilterAspect {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TenantFilterAspect.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Around("execution(* *(..)) && @annotation(com.amachi.app.core.common.annotation.TenantAware)")
    public Object applyTenantFilter(ProceedingJoinPoint pjp) throws Throwable {
        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            log.error("❌ Critical: Tenant not set in context for execution of: {}", pjp.getSignature().toShortString());
            throw new RuntimeException("Tenant not set in context");
        }

        Session session = entityManager.unwrap(Session.class);

        try {
            session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
            log.trace("🛡️ Tenant Isolation ACTIVE for TenantID: {}", tenantId);

            return pjp.proceed();

        } finally {
            try {
                session.disableFilter("tenantFilter");
            } catch (Exception e) {
                // Silently ignore
            }
        }
    }
}
