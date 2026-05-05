package com.amachi.app.core.management.avatar.entity;

import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.core.common.entity.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

/**
 * Almacenamiento binario y metadata de avatares (SaaS Elite Tier).
 * Ubicada en core-management por ser un recurso transversal de perfil de usuario.
 */
@Entity
@Table(name = "MGT_AVATAR", indexes = {
    @Index(name = "IDX_AVATAR_USER", columnList = "USER_ID"),
    @Index(name = "IDX_AVATAR_TENANT", columnList = "TENANT_ID")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Gestión de avatares y recursos multimedia — SaaS Elite Tier")
public class Avatar extends AuditableTenantEntity implements Model {


    @Column(name = "FILENAME", length = 255)
    private String filename;

    @Column(name = "MIME_TYPE", length = 100)
    private String mimeType;

    @Lob
    @Column(name = "CONTENT", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] content;

    @Column(name = "SIZE")
    private Long size;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_AVATAR_USER"))
    private User user;
}
