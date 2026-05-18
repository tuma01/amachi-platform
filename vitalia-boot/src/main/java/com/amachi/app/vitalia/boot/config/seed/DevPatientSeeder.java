package com.amachi.app.vitalia.boot.config.seed;

import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.enums.*;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.entity.PersonTenant;
import com.amachi.app.core.domain.repository.PersonRepository;
import com.amachi.app.core.domain.repository.PersonTenantRepository;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.domain.tenant.repository.TenantRepository;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.entity.HealthcareProvider;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.repository.HealthcareProviderRepository;
import com.amachi.app.vitalia.medicalcore.insurance.entity.Insurance;
import com.amachi.app.vitalia.medicalcore.insurance.repository.InsuranceRepository;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.MedicalHistory;
import com.amachi.app.vitalia.medicalcore.medicalhistory.repository.MedicalHistoryRepository;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import com.amachi.app.vitalia.medicalcore.patient.entity.PatientDetails;
import com.amachi.app.vitalia.medicalcore.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Siembra pacientes con su expediente clínico y seguro médico.
 *
 * Datos sembrados:
 *   PAC-DEV-001 → Juan Carlos García López (41 años, Medicina Interna)
 *                 Expediente HC-2026-JG-001
 *                 Seguro CNS — POL-CNS-2026-001
 */
@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevPatientSeeder {

    private static final String TENANT_CODE = "hospital-san-borja";
    private static final String GUARD_EMAIL = "pac-dev@test.com";

    private final TenantRepository              tenantRepository;
    private final PersonRepository              personRepository;
    private final PersonTenantRepository        personTenantRepository;
    private final PatientRepository             patientRepository;
    private final MedicalHistoryRepository      medicalHistoryRepository;
    private final InsuranceRepository           insuranceRepository;
    private final HealthcareProviderRepository  providerRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    @Order(30)
    public void seed() {
        log.info("🧑‍⚕️ [PATIENT-SEEDER] Verificando pacientes...");

        Tenant tenant = tenantRepository.findByCode(TENANT_CODE).orElse(null);
        if (tenant == null) {
            log.warn("⚠️ [PATIENT-SEEDER] Tenant '{}' no encontrado.", TENANT_CODE);
            return;
        }

        TenantContext.setTenantId(tenant.getId());
        TenantContext.setTenantCode(tenant.getCode());

        try {
            if (personRepository.findByEmail(GUARD_EMAIL).isPresent()) {
                log.info("⏭️  [PATIENT-SEEDER] Pacientes ya existen. Omitido.");
                return;
            }

            Patient patient = seedPatientJuanGarcia(tenant);
            MedicalHistory history = seedMedicalHistory(patient);
            providerRepository.findByCode("CNS-BOL").ifPresent(cns -> seedInsurance(history, cns));

            log.info("✅ [PATIENT-SEEDER] Pacientes y expedientes creados.");
            log.info("   → Juan García  (PAC-DEV-001) — HC-2026-JG-001 — Seguro CNS");

        } catch (Exception ex) {
            log.error("❌ [PATIENT-SEEDER] Error: {}", ex.getMessage(), ex);
        } finally {
            TenantContext.clear();
        }
    }

    private Patient seedPatientJuanGarcia(Tenant tenant) {
        Person person = personRepository.save(Person.builder()
                .firstName("Juan").middleName("Carlos")
                .lastName("García").secondLastName("López")
                .nationalId("10000001").email("pac-dev@test.com")
                .documentType(DocumentType.DNI).gender(Gender.MALE)
                .birthDate(LocalDate.of(1985, 6, 15)).build());

        personTenantRepository.save(PersonTenant.builder()
                .person(person).tenant(tenant).roleContext(RoleContext.PATIENT)
                .dateRegistered(LocalDateTime.now()).relationStatus(RelationStatus.ACTIVE).build());

        return patientRepository.save(Patient.builder()
                .person(person).nhc("PAC-DEV-001")
                .patientStatus(PatientStatus.ACTIVE).active(true)
                .nationality("BOLIVIANA").occupation("Empleado Público")
                .details(PatientDetails.builder().hasDisability(false).isPregnant(false).build())
                .emergencyContactName("María García")
                .emergencyContactPhone("+591 70000001")
                .emergencyContactRelationship("Cónyuge").build());
    }

    private MedicalHistory seedMedicalHistory(Patient patient) {
        return medicalHistoryRepository.save(MedicalHistory.builder()
                .historyNumber("HC-2026-JG-001").patient(patient)
                .recordDate(LocalDate.of(2026, 1, 10))
                .isCurrent(true).isOrganDonor(false).isLocked(false)
                .confidentialityLevel("NORMAL")
                .observations("Paciente masculino, 41 años. HTA leve controlada con dieta. " +
                              "Alérgico a la penicilina.")
                .notes("Expediente de desarrollo — datos de prueba.").build());
    }

    private void seedInsurance(MedicalHistory history, HealthcareProvider provider) {
        insuranceRepository.save(Insurance.builder()
                .medicalHistory(history).provider(provider)
                .policyNumber("POL-CNS-2026-001")
                .policyType("SEGURO SOCIAL OBLIGATORIO")
                .effectiveDate(LocalDate.of(2026, 1, 1))
                .expirationDate(LocalDate.of(2026, 12, 31))
                .copayAmount(new BigDecimal("20.00"))
                .deductibleAmount(BigDecimal.ZERO)
                .requiresAuthorization(true).isCurrent(true)
                .coverageDetails("Cobertura integral CNS: consulta, especialidades, " +
                                 "hospitalización, cirugías y medicamentos del listado básico.")
                .build());
    }
}
