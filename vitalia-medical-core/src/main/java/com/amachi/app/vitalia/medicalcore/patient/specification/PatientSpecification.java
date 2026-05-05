package com.amachi.app.vitalia.medicalcore.patient.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.patient.dto.search.PatientSearchDto;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * Lógica de búsqueda ÉLITE para Pacientes usando Criteria API.
 * Sigue el patrón validado de AppointmentSpecification.
 */
//@AllArgsConstructor
public class PatientSpecification extends BaseSpecification<Patient> implements Specification<Patient> {

    private transient PatientSearchDto criteria;

    public PatientSpecification(PatientSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Patient> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // 🛡️ Filtros Base Automáticos (TENANT_ID e IS_DELETED) via BaseSpecification
        predicates.addAll(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        // Filtro de búsqueda textual (Nombre o Apellidos)
        if (criteria.getQuery() != null && !criteria.getQuery().isBlank()) {
            String q = "%" + criteria.getQuery().toLowerCase() + "%";
            predicates.add(cb.or(
                cb.like(cb.lower(root.join("person").get("firstName")), q),
                cb.like(cb.lower(root.join("person").get("lastName")), q)
            ));
        }

        // Filtro por EXTERNAL_ID
        if (criteria.getExternalId() != null && !criteria.getExternalId().isBlank()) {
            predicates.add(cb.equal(root.get("externalId"), criteria.getExternalId()));
        }

        // Filtro por Documento de Identidad (nationalId)
        if (criteria.getNationalId() != null && !criteria.getNationalId().isBlank()) {
            predicates.add(cb.equal(root.join("person").get("nationalId"), criteria.getNationalId()));
        }

        // Filtro por NHC
        if (criteria.getNhc() != null && !criteria.getNhc().isBlank()) {
             predicates.add(cb.equal(root.get("nhc"), criteria.getNhc()));
        }

        // Filtro por Email
        if (criteria.getEmail() != null && !criteria.getEmail().isBlank()) {
            predicates.add(cb.equal(cb.lower(root.join("person").get("email")), criteria.getEmail().toLowerCase()));
        }

        // Filtro por Estado
        if (criteria.getPatientStatus() != null) {
            predicates.add(cb.equal(root.get("patientStatus"), criteria.getPatientStatus()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
