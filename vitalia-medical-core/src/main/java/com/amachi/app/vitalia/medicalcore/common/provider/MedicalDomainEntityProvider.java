package com.amachi.app.vitalia.medicalcore.common.provider;

import com.amachi.app.core.common.enums.DomainContext;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.person.service.DomainEntityProvider;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import com.amachi.app.vitalia.medicalcore.doctor.repository.DoctorRepository;
import com.amachi.app.vitalia.medicalcore.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Proveedor de entidades de dominio médico (SaaS Elite Tier).
 * Orquesta la creación de registros de Doctor y Paciente durante el onboarding.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MedicalDomainEntityProvider implements DomainEntityProvider {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    public boolean supports(DomainContext context) {
        return context == DomainContext.DOCTOR || context == DomainContext.PATIENT;
    }

    @Override
    @Transactional
    public void createEntity(Person person, Tenant tenant, DomainContext context) {
        log.info("[MEDICAL-PROVIDER] Creating clinical entity for context: {} (Person: {}, Tenant: {})", 
                context, person.getId(), tenant.getCode());

        switch (context) {
            case DOCTOR -> createDoctor(person, tenant);
            case PATIENT -> createPatient(person, tenant);
            default -> throw new IllegalArgumentException("Unsupported clinical context: " + context);
        }
    }

    @Override
    public boolean exists(Person person, Tenant tenant, DomainContext context) {
        return switch (context) {
            case DOCTOR -> doctorRepository.existsByPersonIdAndTenantId(person.getId(), tenant.getId());
            case PATIENT -> patientRepository.existsByPersonIdAndTenantId(person.getId(), tenant.getId());
            default -> false;
        };
    }

    private void createDoctor(Person person, Tenant tenant) {
        if (!doctorRepository.existsByPersonIdAndTenantId(person.getId(), tenant.getId())) {
            log.debug("[MEDICAL-PROVIDER] Persisting new Doctor for person: {}", person.getId());
            Doctor doctor = Doctor.builder()
                    .person(person)
                    .tenantId(tenant.getId())
                    .tenantCode(tenant.getCode())
                    .isActive(true)
                    .licenseNumber("PENDING-" + java.util.UUID.randomUUID()) // Placeholder único
                    .build();
            doctorRepository.save(doctor);
        }
    }

    private void createPatient(Person person, Tenant tenant) {
        if (!patientRepository.existsByPersonIdAndTenantId(person.getId(), tenant.getId())) {
            log.debug("[MEDICAL-PROVIDER] Persisting new Patient for person: {}", person.getId());
            Patient patient = Patient.builder()
                    .person(person)
                    .tenantId(tenant.getId())
                    .tenantCode(tenant.getCode())
                    .nhc("NHC-NEW-" + java.util.UUID.randomUUID()) // Generar NHC temporal único
                    .identificationNumber(person.getNationalId())
                    .build();
            patientRepository.save(patient);
        }
    }
}
