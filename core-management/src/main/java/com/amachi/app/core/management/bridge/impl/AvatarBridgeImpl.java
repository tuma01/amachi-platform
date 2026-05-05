package com.amachi.app.core.management.bridge.impl;

import com.amachi.app.core.auth.bridge.AvatarBridge;
import com.amachi.app.core.management.avatar.entity.Avatar;
import com.amachi.app.core.management.avatar.service.AvatarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class AvatarBridgeImpl implements AvatarBridge {

    private final AvatarService avatarService;

    @Override
    public void createDefaultAvatar(Long userId) {
        avatarService.createDefault(userId);
    }

    @Override
    public void updateAvatar(Long userId, MultipartFile file) {
        try {
            avatarService.upload(userId, file);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update avatar for userId=" + userId, e);
        }
    }

    @Override
    public void deleteAvatar(Long userId) {
        avatarService.deleteByUserId(userId);
    }

    @Override
    public byte[] getAvatar(Long userId) {
        return avatarService.findByUserId(userId)
                .map(Avatar::getContent)
                .orElse(new byte[0]);
    }
}
