package com.amachi.app.vitalia.medicalcore.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Canal de entrega para recordatorios omnicanal de citas y eventos médicos.
 * Soporta múltiples vías de notificación al paciente o familiar.
 */
@Schema(description = "Canal de comunicación para recordatorios médicos")
public enum ReminderChannel {
    SMS,         // Mensaje de texto vía red móvil
    EMAIL,       // Correo electrónico
    WHATSAPP,    // Mensajería instantánea WhatsApp Business
    APP_PUSH,    // Notificación push de la aplicación móvil Vitalia
    VOICE_CALL   // Llamada de voz automatizada (IVR)
}
