package com.amachi.app.core.auth.bridge;


public interface AvatarBridge {

    /**
     * Crea un avatar por defecto para el usuario en el tenant indicado.
     */
    void createDefaultAvatar(Long userId);

    void updateAvatar(Long userId, org.springframework.web.multipart.MultipartFile file);

    void deleteAvatar(Long userId);

    byte[] getAvatar(Long userId);
}
