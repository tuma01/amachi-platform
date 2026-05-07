package com.amachi.app.core.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

/**
 * Encriptación AES-256/CBC de campos PHI en base de datos.
 * IV determinístico: derivado de SHA-256(key)[0..15], garantizando
 * que el mismo plaintext produce siempre el mismo ciphertext.
 * Esto preserva las constraints UNIQUE de la BD (ej: NHC).
 *
 * La clave se inyecta via EncryptionKeyHolder desde la propiedad
 * app.phi.encryption-key (Base64, 32 bytes / 256 bits).
 *
 * Uso: @Convert(converter = EncryptedStringConverter.class) sobre el campo.
 * ISO 27001 A.10.1 / HIPAA §164.312(a)(2)(iv).
 */
@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    private static final Logger log = LoggerFactory.getLogger(EncryptedStringConverter.class);
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    @Override
    public String convertToDatabaseColumn(String plaintext) {
        if (plaintext == null || plaintext.isBlank()) return plaintext;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, buildKey(), buildIv());
            byte[] encrypted = cipher.doFinal(plaintext.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("Error encriptando campo PHI", e);
            throw new IllegalStateException("PHI encryption failed", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String ciphertext) {
        if (ciphertext == null || ciphertext.isBlank()) return ciphertext;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, buildKey(), buildIv());
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
            return new String(decrypted, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Error desencriptando campo PHI", e);
            throw new IllegalStateException("PHI decryption failed", e);
        }
    }

    private SecretKeySpec buildKey() {
        String keyBase64 = EncryptionKeyHolder.getEncryptionKey();
        if (keyBase64 == null || keyBase64.isBlank()) {
            throw new IllegalStateException("app.phi.encryption-key no configurada");
        }
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        if (keyBytes.length != 32) {
            throw new IllegalStateException("La clave PHI debe ser de 32 bytes (256 bits)");
        }
        return new SecretKeySpec(keyBytes, "AES");
    }

    private IvParameterSpec buildIv() throws Exception {
        String keyBase64 = EncryptionKeyHolder.getEncryptionKey();
        byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha.digest(keyBytes);
        return new IvParameterSpec(Arrays.copyOf(hash, 16));
    }
}
