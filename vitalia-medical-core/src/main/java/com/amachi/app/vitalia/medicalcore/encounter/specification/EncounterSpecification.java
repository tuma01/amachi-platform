package com.amachi.app.vitalia.medicalcore.encounter.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.encounter.dto.search.EncounterSearchDto;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Motor de búsqueda avanzada para Encuentros Clínicos (SaaS Elite Tier).
 */
public class EncounterSpecification extends BaseSpecification<Encounter> {
 
    private final EncounterSearchDto criteria;

    public EncounterSpecification(EncounterSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Encounter> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // 🛡️ Filtros Base Automáticos (TENANT_ID e IS_DELETED) via BaseSpecification helper
        predicates.addAll(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getPatientId() != null) {
            predicates.add(cb.equal(root.get("patient").get("id"), criteria.getPatientId()));
        }

        if (criteria.getDoctorId() != null) {
            predicates.add(cb.equal(root.get("doctor").get("id"), criteria.getDoctorId()));
        }

        if (criteria.getMedicalHistoryId() != null) {
            predicates.add(cb.equal(root.get("medicalHistory").get("id"), criteria.getMedicalHistoryId()));
        }

        if (criteria.getEpisodeOfCareId() != null) {
            predicates.add(cb.equal(root.get("episodeOfCare").get("id"), criteria.getEpisodeOfCareId()));
        }

        if (criteria.getAppointmentId() != null) {
            predicates.add(cb.equal(root.get("appointment").get("id"), criteria.getAppointmentId()));
        }

        if (criteria.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
        }

        if (criteria.getEncounterType() != null) {
            predicates.add(cb.equal(root.get("encounterType"), clinicalEnumValue(criteria.getEncounterType())));
        }

        if (criteria.getEncounterDateFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("encounterDate"), criteria.getEncounterDateFrom()));
        }

        if (criteria.getEncounterDateTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("encounterDate"), criteria.getEncounterDateTo()));
        }

        if (criteria.getTriageLevel() != null && !criteria.getTriageLevel().isBlank()) {
            predicates.add(cb.equal(root.get("triageLevel"), criteria.getTriageLevel()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private String clinicalEnumValue(Enum<?> value) {
        return value != null ? value.name() : null;
    }
}
