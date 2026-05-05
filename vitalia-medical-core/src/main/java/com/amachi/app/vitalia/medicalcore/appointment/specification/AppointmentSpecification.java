package com.amachi.app.vitalia.medicalcore.appointment.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.appointment.dto.search.AppointmentSearchDto;
import com.amachi.app.vitalia.medicalcore.appointment.entity.Appointment;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Motor de búsqueda dinámico para la agenda médica (SaaS Elite Tier).
 * Garantiza el aislamiento multi-inquilino y el filtrado de registros eliminados.
 */
@AllArgsConstructor
public class AppointmentSpecification extends BaseSpecification<Appointment> {

    private transient AppointmentSearchDto criteria;

    @Override
    public Predicate toPredicate(Root<Appointment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // 🛡️ Filtros Base Automáticos (TENANT_ID e IS_DELETED) via BaseSpecification
        predicates.addAll(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getId() != null) {
            predicates.add(cb.equal(root.get("id"), criteria.getId()));
        }

        if (criteria.getPatientId() != null) {
            predicates.add(cb.equal(root.get("patient").get("id"), criteria.getPatientId()));
        }

        if (criteria.getDoctorId() != null) {
            predicates.add(cb.equal(root.get("doctor").get("id"), criteria.getDoctorId()));
        }

        if (criteria.getRoomId() != null) {
            predicates.add(cb.equal(root.get("room").get("id"), criteria.getRoomId()));
        }

        if (criteria.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
        }

        if (criteria.getSource() != null) {
            predicates.add(cb.equal(root.get("source"), criteria.getSource()));
        }

        if (criteria.getNoShow() != null) {
            predicates.add(cb.equal(root.get("noShow"), criteria.getNoShow()));
        }

        // Motor de agenda: filtrado por rango de tiempo
        if (criteria.getStartFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("startTime"), criteria.getStartFrom()));
        }

        if (criteria.getStartTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("startTime"), criteria.getStartTo()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
