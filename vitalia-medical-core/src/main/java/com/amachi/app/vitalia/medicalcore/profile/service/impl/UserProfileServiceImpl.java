package com.amachi.app.vitalia.medicalcore.profile.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.profile.dto.UserProfileDto;
import com.amachi.app.vitalia.medicalcore.profile.dto.search.UserProfileSearchDto;
import com.amachi.app.vitalia.medicalcore.profile.entity.UserProfile;
import com.amachi.app.vitalia.medicalcore.profile.mapper.UserProfileMapper;
import com.amachi.app.vitalia.medicalcore.profile.repository.UserProfileRepository;
import com.amachi.app.vitalia.medicalcore.profile.service.UserProfileService;
import com.amachi.app.vitalia.medicalcore.profile.specification.UserProfileSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileServiceImpl extends BaseService<UserProfile, UserProfileDto, UserProfileSearchDto>
        implements UserProfileService {

    private final UserProfileRepository repository;
    private final UserProfileMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected CommonRepository<UserProfile, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<UserProfile> buildSpecification(UserProfileSearchDto searchDto) {
        return new UserProfileSpecification(searchDto);
    }

    @Override
    @Transactional
    public UserProfile create(UserProfileDto dto) {
        if (dto == null) throw new BusinessException("UserProfile data cannot be null");
        return repository.save(mapper.toEntity(dto));
    }

    @Override
    protected void mergeEntities(UserProfileDto dto, UserProfile existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected UserProfile mapToEntity(UserProfileDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void publishCreatedEvent(UserProfile entity) { }

    @Override
    protected void publishUpdatedEvent(UserProfile entity) { }
}
