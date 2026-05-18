package com.amachi.app.vitalia.boot.config.seed;

import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.enums.*;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.entity.PersonTenant;
import com.amachi.app.core.domain.repository.PersonRepository;
import com.amachi.app.core.domain.repository.PersonTenantRepository;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.domain.tenant.repository.TenantRepository;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.doctor.repository.DoctorRepository;
import com.amachi.app.vitalia.medicalcore.employee.entity.Employee;
import com.amachi.app.vitalia.medicalcore.employee.repository.EmployeeRepository;
import com.amachi.app.vitalia.medicalcore.nurse.entity.Nurse;
import com.amachi.app.vitalia.medicalcore.nurse.repository.NurseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Siembra el personal clínico y administrativo del tenant de desarrollo.
 *
 * Datos sembrados:
 *   doctor-dev@test.com  → Dr. Carlos Mendoza (Medicina General)
 *   nurse-dev@test.com   → Enf. Ana Torres (Enfermera Jefe)
 *   admin-emp-dev@test.com → Luis Pérez (Jefe Administrativo)
 */
@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevStaffSeeder {

    private static final String TENANT_CODE    = "hospital-san-borja";
    private static final String GUARD_EMAIL    = "doctor-dev@test.com";

    private final TenantRepository       tenantRepository;
    private final PersonRepository       personRepository;
    private final PersonTenantRepository personTenantRepository;
    private final DoctorRepository       doctorRepository;
    private final NurseRepository        nurseRepository;
    private final EmployeeRepository     employeeRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    @Order(20)
    public void seed() {
        log.info("👨‍⚕️ [STAFF-SEEDER] Verificando personal clínico...");

        Tenant tenant = tenantRepository.findByCode(TENANT_CODE).orElse(null);
        if (tenant == null) {
            log.warn("⚠️ [STAFF-SEEDER] Tenant '{}' no encontrado.", TENANT_CODE);
            return;
        }

        TenantContext.setTenantId(tenant.getId());
        TenantContext.setTenantCode(tenant.getCode());

        try {
            if (personRepository.findByEmail(GUARD_EMAIL).isPresent()) {
                log.info("⏭️  [STAFF-SEEDER] Personal ya existe. Omitido.");
                return;
            }

            seedDoctor(tenant);
            seedNurse(tenant);
            seedEmployee(tenant);

            log.info("✅ [STAFF-SEEDER] Personal creado.");
            log.info("   → Dr. Carlos Mendoza  (LIC-DEV-001) — Medicina General");
            log.info("   → Enf. Ana Torres     (NRS-DEV-001) — Enfermera Jefe");
            log.info("   → Luis Pérez          (ADM-DEV-001) — Jefe Administrativo");

        } catch (Exception ex) {
            log.error("❌ [STAFF-SEEDER] Error: {}", ex.getMessage(), ex);
        } finally {
            TenantContext.clear();
        }
    }

    private void seedDoctor(Tenant tenant) {
        Person person = personRepository.save(Person.builder()
                .firstName("Carlos").lastName("Mendoza").secondLastName("Quispe")
                .nationalId("10000002").email("doctor-dev@test.com")
                .documentType(DocumentType.DNI).gender(Gender.MALE)
                .birthDate(LocalDate.of(1978, 3, 22)).build());

        personTenantRepository.save(PersonTenant.builder()
                .person(person).tenant(tenant).roleContext(RoleContext.DOCTOR)
                .dateRegistered(LocalDateTime.now()).relationStatus(RelationStatus.ACTIVE).build());

        doctorRepository.save(Doctor.builder()
                .person(person).licenseNumber("LIC-DEV-001")
                .specialtiesSummary("Medicina General, Medicina Interna")
                .hireDate(LocalDate.of(2022, 3, 1))
                .yearsOfExperience(10).isActive(true).build());
    }

    private void seedNurse(Tenant tenant) {
        Person person = personRepository.save(Person.builder()
                .firstName("Ana").middleName("María").lastName("Torres").secondLastName("Mamani")
                .nationalId("10000003").email("nurse-dev@test.com")
                .documentType(DocumentType.DNI).gender(Gender.FEMALE)
                .birthDate(LocalDate.of(1990, 7, 14)).build());

        personTenantRepository.save(PersonTenant.builder()
                .person(person).tenant(tenant).roleContext(RoleContext.NURSE)
                .dateRegistered(LocalDateTime.now()).relationStatus(RelationStatus.ACTIVE).build());

        nurseRepository.save(Nurse.builder()
                .person(person).licenseNumber("NRS-DEV-001")
                .rank("ENFERMERA JEFE").workShift(ShiftEnum.MORNING)
                .hireDate(LocalDate.of(2022, 3, 1)).contractType("FULL_TIME").build());
    }

    private void seedEmployee(Tenant tenant) {
        Person person = personRepository.save(Person.builder()
                .firstName("Luis").lastName("Pérez").secondLastName("Salazar")
                .nationalId("10000004").email("admin-emp-dev@test.com")
                .documentType(DocumentType.DNI).gender(Gender.MALE)
                .birthDate(LocalDate.of(1982, 11, 5)).build());

        personTenantRepository.save(PersonTenant.builder()
                .person(person).tenant(tenant).roleContext(RoleContext.EMPLOYEE)
                .dateRegistered(LocalDateTime.now()).relationStatus(RelationStatus.ACTIVE).build());

        employeeRepository.save(Employee.builder()
                .person(person).employeeType(EmployeeType.ADMINISTRATIVO)
                .employeeStatus(EmployeeStatus.ACTIVO).employeeCode("ADM-DEV-001")
                .jobPosition("Jefe Administrativo").hireDate(LocalDate.of(2022, 3, 1))
                .workShift(ShiftEnum.ADMINISTRATIVE).employmentType("FULL_TIME").build());
    }
}
