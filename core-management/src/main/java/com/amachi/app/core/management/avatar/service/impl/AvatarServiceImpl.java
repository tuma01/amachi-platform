package com.amachi.app.core.management.avatar.service.impl;

import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.ResourceNotFoundException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.core.management.avatar.dto.AvatarDto;
import com.amachi.app.core.management.avatar.dto.search.AvatarSearchDto;
import com.amachi.app.core.management.avatar.entity.Avatar;
import com.amachi.app.core.management.avatar.repository.AvatarRepository;
import com.amachi.app.core.management.avatar.service.AvatarService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@TenantAware
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AvatarServiceImpl extends BaseService<Avatar, AvatarDto, AvatarSearchDto>
        implements AvatarService {

    private final AvatarRepository avatarRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private EntityManager em;

    @Override
    protected CommonRepository<Avatar, Long> getRepository() {
        return avatarRepository;
    }

    @Override
    protected Specification<Avatar> buildSpecification(AvatarSearchDto searchDto) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            if (searchDto.getUserId() != null) {
                predicates.add(cb.equal(root.get("user").get("id"), searchDto.getUserId()));
            }
            if (searchDto.getMimeType() != null && !searchDto.getMimeType().isBlank()) {
                predicates.add(cb.equal(root.get("mimeType"), searchDto.getMimeType()));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    @Override
    protected DomainEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    protected Avatar mapToEntity(AvatarDto dto) {
        return Avatar.builder()
                .filename(dto.getFilename())
                .mimeType(dto.getMimeType())
                .size(dto.getSize())
                .build();
    }

    @Override
    protected void mergeEntities(AvatarDto dto, Avatar existing) {
        if (dto.getFilename() != null) existing.setFilename(dto.getFilename());
        if (dto.getMimeType() != null) existing.setMimeType(dto.getMimeType());
        if (dto.getSize() != null)     existing.setSize(dto.getSize());
    }

    @Override
    protected void publishCreatedEvent(Avatar entity) {
        log.info("📷 Avatar created for userId={}", entity.getUser() != null ? entity.getUser().getId() : null);
    }

    @Override
    protected void publishUpdatedEvent(Avatar entity) {
        log.info("📷 Avatar updated for userId={}", entity.getUser() != null ? entity.getUser().getId() : null);
    }

    @Override
    @Transactional
    public void createDefault(Long userId) {
        if (avatarRepository.existsByUserId(userId)) {
            return;
        }
        User userRef = em.getReference(User.class, userId);
        Avatar avatar = Avatar.builder()
                .user(userRef)
                .filename("default-avatar.png")
                .mimeType("image/png")
                .size(0L)
                .content(new byte[0])
                .build();
        avatarRepository.save(avatar);
        log.info("📷 Default avatar created for userId={}", userId);
    }

    @Override
    @Transactional
    public Avatar upload(Long userId, MultipartFile file) throws IOException {
        User userRef = em.getReference(User.class, userId);

        Avatar avatar = avatarRepository.findByUserId(userId)
                .orElse(Avatar.builder().user(userRef).build());

        avatar.setFilename(file.getOriginalFilename());
        avatar.setMimeType(file.getContentType());
        avatar.setSize(file.getSize());
        avatar.setContent(file.getBytes());

        Avatar saved = avatarRepository.save(avatar);
        log.info("📷 Avatar uploaded for userId={}, size={}B", userId, file.getSize());
        return saved;
    }

    @Override
    public Optional<Avatar> findByUserId(Long userId) {
        return avatarRepository.findByUserId(userId);
    }

    @Override
    public byte[] getContent(Long userId) {
        return avatarRepository.findByUserId(userId)
                .map(Avatar::getContent)
                .orElseThrow(() -> new ResourceNotFoundException("Avatar", "error.resource.not.found", userId));
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        avatarRepository.deleteByUserId(userId);
        log.info("🗑️ Avatar deleted for userId={}", userId);
    }
}
