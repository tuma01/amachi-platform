package com.amachi.app.core.common.converter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Puente entre Spring y los JPA AttributeConverters.
 * JPA instancia los converters fuera del contexto de Spring, por lo que
 * no puede usar @Value directamente. Este holder se inicializa en startup
 * y expone la clave via campo estático.
 *
 * ISO 27001 A.10.1 — Gestión de claves criptográficas.
 * La clave se lee desde variable de entorno APP_PHI_ENCRYPTION_KEY.
 */
@Component
public class EncryptionKeyHolder {

    private static String encryptionKey;

    @Value("${app.phi.encryption-key}")
    public void setEncryptionKey(String key) {
        EncryptionKeyHolder.encryptionKey = key;
    }

    public static String getEncryptionKey() {
        return encryptionKey;
    }
}
