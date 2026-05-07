package com.amachi.app.vitalia.medicalcore.appointment.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.appointment.dto.search.AppointmentReminderSearchDto;
import com.amachi.app.vitalia.medicalcore.appointment.entity.AppointmentReminder;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class AppointmentReminderSpecification extends BaseSpecification<AppointmentReminder> {

    private final AppointmentReminderSearchDto criteria;

    public AppointmentReminderSpecification(AppointmentReminderSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<AppointmentReminder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getAppointmentId() != null)
            predicates.add(cb.equal(root.get("appointment").get("id"), criteria.getAppointmentId()));

        if (criteria.getChannel() != null)
            predicates.add(cb.equal(root.get("channel"), criteria.getChannel()));

        if (criteria.getStatus() != null)
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
