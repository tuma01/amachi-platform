package com.amachi.app.vitalia.medicalcore.profile.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.profile.dto.UserProfileDto;
import com.amachi.app.vitalia.medicalcore.profile.dto.search.UserProfileSearchDto;
import com.amachi.app.vitalia.medicalcore.profile.entity.UserProfile;

public interface UserProfileService extends GenericService<UserProfile, UserProfileDto, UserProfileSearchDto> {
}
