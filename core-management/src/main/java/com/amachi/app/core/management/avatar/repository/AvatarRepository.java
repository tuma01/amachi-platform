package com.amachi.app.core.management.avatar.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.core.management.avatar.entity.Avatar;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvatarRepository extends TenantCommonRepository<Avatar, Long> {

    Optional<Avatar> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    void deleteByUserId(Long userId);
}
