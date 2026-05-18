package com.amachi.app.vitalia.boot.config.seed;

import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.enums.BedStatusEnum;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.domain.tenant.repository.TenantRepository;
import com.amachi.app.vitalia.medicalcore.common.enums.*;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.doctor.repository.DoctorRepository;
import com.amachi.app.vitalia.medicalcore.hospitalization.entity.Hospitalization;
import com.amachi.app.vitalia.medicalcore.hospitalization.repository.HospitalizationRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Bed;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.DepartmentUnit;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Room;
import com.amachi.app.vitalia.medicalcore.infrastructure.repository.BedRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.repository.DepartmentUnitRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.repository.RoomRepository;
import com.amachi.app.vitalia.medicalcore.insurance.entity.Insurance;
import com.amachi.app.vitalia.medicalcore.insurance.repository.InsuranceRepository;
import com.amachi.app.vitalia.medicalcore.nurse.entity.Nurse;
import com.amachi.app.vitalia.medicalcore.nurse.repository.NurseRepository;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import com.amachi.app.vitalia.medicalcore.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Siembra hospitalizaciones activas para probar el flujo completo de admisión.
 * Depende de: DevInfrastructureSeeder (@Order 10) y DevPatientSeeder (@Order 30).
 *
 * Datos sembrados:
 *   HOSP-001 → Juan García, Medicina Interna Hab.201 Cama B, ACTIVE
 *              J18.9 Neumonía adquirida en la comunidad
 *              Seguro: CNS (POL-CNS-2026-001) → Autorización: CNS-AUTH-2026-00547
 */
@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevAdmissionSeeder {

    private static final String TENANT_CODE = "hospital-san-borja";

    private final TenantRepository          tenantRepository;
    private final PatientRepository         patientRepository;
    private final DoctorRepository          doctorRepository;
    private final NurseRepository           nurseRepository;
    private final DepartmentUnitRepository  unitRepository;
    private final RoomRepository            roomRepository;
    private final BedRepository             bedRepository;
    private final HospitalizationRepository hospitalizationRepository;
    private final InsuranceRepository       insuranceRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    @Order(40)
    public void seed() {
        log.info("🏨 [ADMISSION-SEEDER] Verificando hospitalizaciones...");

        Tenant tenant = tenantRepository.findByCode(TENANT_CODE).orElse(null);
        if (tenant == null) {
            log.warn("⚠️ [ADMISSION-SEEDER] Tenant '{}' no encontrado.", TENANT_CODE);
            return;
        }

        TenantContext.setTenantId(tenant.getId());
        TenantContext.setTenantCode(tenant.getCode());

        try {
            seedJuanGarciaHospitalization(tenant.getId());

        } catch (Exception ex) {
            log.error("❌ [ADMISSION-SEEDER] Error: {}", ex.getMessage(), ex);
        } finally {
            TenantContext.clear();
        }
    }

    private void seedJuanGarciaHospitalization(Long tenantId) {
        Patient patient = patientRepository.findAll().stream()
                .filter(p -> "PAC-DEV-001".equals(p.getNhc()))
                .findFirst().orElse(null);

        if (patient == null) {
            log.info("⏭️  [ADMISSION-SEEDER] Paciente PAC-DEV-001 no encontrado. Omitido.");
            return;
        }

        if (hospitalizationRepository.existsByPatientIdAndStatusAndTenantId(
                patient.getId(), HospitalizationStatus.ACTIVE, tenantId)) {
            log.info("⏭️  [ADMISSION-SEEDER] Hospitalización activa ya existe. Omitido.");
            return;
        }

        Doctor doctor = doctorRepository.findByLicenseNumberAndTenantId("LIC-DEV-001", tenantId).orElse(null);
        Nurse  nurse  = nurseRepository.findByLicenseNumberAndTenantId("NRS-DEV-001", tenantId).orElse(null);

        DepartmentUnit unit = unitRepository.findByCodeAndTenantId("MED-INT", tenantId).orElse(null);
        if (unit == null) { log.warn("⚠️  [ADMISSION-SEEDER] Unidad MED-INT no encontrada."); return; }

        Room room = roomRepository.findByUnitIdAndTenantId(unit.getId(), tenantId).stream()
                .filter(r -> "201".equals(r.getRoomNumber())).findFirst().orElse(null);
        if (room == null) { log.warn("⚠️  [ADMISSION-SEEDER] Habitación 201 no encontrada."); return; }

        Bed bed = bedRepository.findByRoomIdAndTenantId(room.getId(), tenantId).stream()
                .filter(b -> "MI-201-B".equals(b.getBedCode())).findFirst().orElse(null);
        if (bed == null) { log.warn("⚠️  [ADMISSION-SEEDER] Cama MI-201-B no encontrada."); return; }

        // Cargar el seguro activo del paciente (CNS — sembrado por DevPatientSeeder)
        Insurance insurance = insuranceRepository
                .findByMedicalHistoryPatientIdAndTenantId(patient.getId(), tenantId)
                .stream()
                .filter(i -> Boolean.TRUE.equals(i.getIsCurrent()))
                .findFirst().orElse(null);

        if (insurance == null) {
            log.warn("⚠️  [ADMISSION-SEEDER] Seguro activo de PAC-DEV-001 no encontrado. " +
                     "Se creará la admisión sin seguro vinculado.");
        }

        hospitalizationRepository.save(Hospitalization.builder()
                .patient(patient).doctor(doctor).nurse(nurse)
                .unit(unit).room(room).bed(bed)
                .insurance(insurance)                           // ← seguro vinculado correctamente
                .admissionDate(OffsetDateTime.of(2026, 5, 15, 9, 30, 0, 0, ZoneOffset.of("-04:00")))
                .estimatedDischargeDate(OffsetDateTime.of(2026, 5, 22, 12, 0, 0, 0, ZoneOffset.of("-04:00")))
                .status(HospitalizationStatus.ACTIVE)
                .priority(HospitalizationPriority.SELECTIVE)
                .admissionType(AdmissionType.PLANNED)
                .admissionDiagnosis("J18.9 — Neumonía, no especificada")
                .admissionReason("Paciente de 41 años con tos productiva, fiebre 38.5°C y disnea de esfuerzo " +
                                 "de 5 días de evolución. Rx tórax con infiltrado en LID compatible con NAC.")
                .treatmentPlan("1. Amoxicilina-Clavulanato 1 g IV c/8h × 7 días.\n" +
                               "2. O₂ por cánula nasal 2 L/min — SatO₂ objetivo ≥ 94%.\n" +
                               "3. SSN 0.9% 1000 ml IV c/12h.\n" +
                               "4. Paracetamol 1 g IV c/8h si T° > 38°C.\n" +
                               "5. Control Rx tórax a las 72 h.\n" +
                               "6. Fisioterapia respiratoria 2 sesiones/día.")
                .insuranceAuthorizationNumber(insurance != null ? "CNS-AUTH-2026-00547" : null)
                .currency("BOB").build());

        bed.setIsOccupied(true);
        bed.setStatus(BedStatusEnum.OCCUPIED);
        bedRepository.save(bed);

        log.info("✅ [ADMISSION-SEEDER] Hospitalización creada.");
        log.info("   → Juan García — Med. Interna Hab.201 Cama B — J18.9 NAC");
        log.info("   → Seguro: {} — Auth: {}",
                insurance != null ? insurance.getPolicyNumber() : "sin seguro",
                insurance != null ? "CNS-AUTH-2026-00547" : "N/A");
    }
}
