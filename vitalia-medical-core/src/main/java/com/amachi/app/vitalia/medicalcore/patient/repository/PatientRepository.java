package com.amachi.app.vitalia.medicalcore.patient.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la gestion de pacientes.
 * Permite la busqueda por identificadores nacionales y clinicos.
 */
@Repository
public interface PatientRepository extends TenantCommonRepository<Patient, Long> {

    /**
     * Busca un paciente por su Numero de Historia Clinica (NHC).
     *
     * @param nhc Numero de Historia Clinica.
     * @param tenantId Identificador del tenant.
     * @return El paciente encontrado o vacio.
     */
    Optional<Patient> findByNhcAndTenantId(String nhc, Long tenantId);

    /**
     * Busca un paciente por su cedula o documento de identidad a traves de Person.
     *
     * @param nationalId Cedula/DNI.
     * @param tenantId Identificador del tenant.
     * @return El paciente encontrado o vacio.
     */
    Optional<Patient> findByPersonNationalIdAndTenantId(String nationalId, Long tenantId);

    /**
     * Busca un paciente por su correo electronico a traves de Person.
     *
     * @param email Correo electronico.
     * @param tenantId Identificador del tenant.
     * @return El paciente encontrado o vacio.
     */
    Optional<Patient> findByPersonEmailAndTenantId(String email, Long tenantId);

    /**
     * Busca un paciente por su Identificador Global Interinstitucional (External ID).
     *
     * @param externalId UUID global.
     * @param tenantId Identificador del tenant.
     * @return El paciente encontrado o vacío.
     */
    Optional<Patient> findByExternalIdAndTenantId(String externalId, Long tenantId);

    @Query("""
    SELECT p FROM Patient p
    WHERE p.tenantId = :tenantId
    AND (
        LOWER(p.person.firstName) LIKE LOWER(CONCAT('%', :text, '%'))
        OR LOWER(p.person.lastName) LIKE LOWER(CONCAT('%', :text, '%'))
    )
    """)
        Page<Patient> searchByName(
                @Param("tenantId") Long tenantId,
                @Param("text") String text,
                Pageable pageable
        );

    boolean existsByPersonIdAndTenantId(Long personId, Long tenantId);
    Optional<Patient> findByPersonIdAndTenantId(Long personId, Long tenantId);
}
