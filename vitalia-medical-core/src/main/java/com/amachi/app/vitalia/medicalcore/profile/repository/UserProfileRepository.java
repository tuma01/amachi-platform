package com.amachi.app.vitalia.medicalcore.profile.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.profile.entity.UserProfile;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends TenantCommonRepository<UserProfile, Long> {
}
