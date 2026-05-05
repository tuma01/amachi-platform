package com.amachi.app.core.management.avatar.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.core.management.avatar.dto.AvatarDto;
import com.amachi.app.core.management.avatar.dto.search.AvatarSearchDto;
import com.amachi.app.core.management.avatar.entity.Avatar;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface AvatarService extends GenericService<Avatar, AvatarDto, AvatarSearchDto> {

    Avatar upload(Long userId, MultipartFile file) throws IOException;

    void createDefault(Long userId);

    Optional<Avatar> findByUserId(Long userId);

    byte[] getContent(Long userId);

    void deleteByUserId(Long userId);
}
