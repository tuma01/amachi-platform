package com.amachi.app.core.auth.repository;

import com.amachi.app.core.auth.entity.UserInvitation;
import com.amachi.app.core.auth.enums.InvitationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.amachi.app.core.common.repository.TenantCommonRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link UserInvitation}.
 *
 * <p>Provides all standard CRUD operations inherited from {@link TenantCommonRepository},
 * plus domain-specific finders required by the invitation lifecycle.
 */
@Repository
public interface UserInvitationRepository extends TenantCommonRepository<UserInvitation, Long> {

    /**
     * Finds an invitation by its one-time token.
     * Used when the invited user clicks the activation link in their email.
     *
     * @param token  the UUID token embedded in the invitation URL
     * @return an {@link Optional} containing the invitation, or empty if not found
     */
    Optional<UserInvitation> findByToken(String token);

    /**
     * Checks whether a PENDING invitation already exists for the given email and tenant.
     * Prevents duplicate invitations to the same user within the same hospital.
     *
     * @param email       the recipient email address
     * @param tenantCode  the tenant code of the target hospital
     * @return {@code true} if a pending invitation already exists
     */
    @Query("""
            SELECT COUNT(i) > 0
            FROM UserInvitation i
            WHERE i.email = :email
              AND i.tenant.code = :tenantCode
              AND i.status = 'PENDING'
              AND i.expiresAt > :now
            """)
    boolean existsPendingInvitation(
            @Param("email") String email,
            @Param("tenantCode") String tenantCode,
            @Param("now") LocalDateTime now
    );

    /**
     * Retrieves all invitations for a given tenant, ordered by creation date descending.
     * Used by the Admin to review pending and historical invitations.
     *
     * @param tenantCode  the code of the tenant to query
     * @return list of invitations belonging to the tenant
     */
    @Query("""
            SELECT i FROM UserInvitation i
            WHERE i.tenant.code = :tenantCode
            ORDER BY i.createdDate DESC
            """)
    List<UserInvitation> findAllByTenantCode(@Param("tenantCode") String tenantCode);

    /**
     * Retrieves an efficiently paginated chunk of target Invitations aligned to Tenant scope restrictions.
     *
     * @param tenantCode structural domain container filtering criterion
     * @param pageable bounded framework metadata for sorting limit injections
     * @return structural container Page of invitation records matching layout request
     */
    @Query("""
            SELECT i FROM UserInvitation i
            WHERE i.tenant.code = :tenantCode
            """)
    Page<UserInvitation> findAllByTenantCode(@Param("tenantCode") String tenantCode, Pageable pageable);

    @Query("""
            SELECT i FROM UserInvitation i
            WHERE i.tenant.code = :tenantCode
              AND i.status = :status
            """)
    Page<UserInvitation> findAllByTenantCodeAndStatus(
            @Param("tenantCode") String tenantCode, 
            @Param("status") InvitationStatus status, 
            Pageable pageable);

    @Query("""
            SELECT i FROM UserInvitation i
            WHERE i.tenant.code = :tenantCode
              AND i.role.name = :roleName
            """)
    Page<UserInvitation> findAllByTenantCodeAndRoleName(
            @Param("tenantCode") String tenantCode, 
            @Param("roleName") String roleName, 
            Pageable pageable);

    @Query("""
            SELECT i FROM UserInvitation i
            WHERE i.tenant.code = :tenantCode
              AND i.status = :status
              AND i.role.name = :roleName
            """)
    Page<UserInvitation> findAllByTenantCodeAndStatusAndRoleName(
            @Param("tenantCode") String tenantCode, 
            @Param("status") InvitationStatus status, 
            @Param("roleName") String roleName, 
            Pageable pageable);

    /**
     * Returns all PENDING invitations whose expiry date has passed.
     * Called by a scheduled task to mark expired invitations automatically.
     *
     * @param now  the reference timestamp (typically {@link LocalDateTime#now()})
     * @return list of expired but still PENDING invitations
     */
    @Query("""
            SELECT i FROM UserInvitation i
            WHERE i.status = :status
              AND i.expiresAt < :now
            """)
    List<UserInvitation> findExpiredInvitations(
            @Param("status") InvitationStatus status,
            @Param("now") LocalDateTime now
    );
}
