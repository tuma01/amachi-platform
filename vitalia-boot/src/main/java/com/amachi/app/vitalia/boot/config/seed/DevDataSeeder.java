package com.amachi.app.vitalia.boot.config.seed;

/**
 * @deprecated Reemplazado por seeders especializados por dominio.
 *
 * Estructura actual del seeding de desarrollo:
 *
 *   DevInfrastructureSeeder  @Order(10) — Unidades, Habitaciones, Camas
 *   DevStaffSeeder           @Order(20) — Doctor, Enfermera, Empleado
 *   DevPatientSeeder         @Order(30) — Paciente, Expediente, Seguro
 *   DevAdmissionSeeder       @Order(40) — Hospitalizaciones
 *
 * Para agregar nuevos datos de prueba: crear un nuevo DevXxxSeeder con @Order(N)
 * y su propia verificación de idempotencia. No modificar los seeders existentes
 * salvo para agregar más registros al mismo dominio.
 */
@Deprecated
public class DevDataSeeder {
    // Clase vacía — ver seeders especializados en este mismo paquete.
}
