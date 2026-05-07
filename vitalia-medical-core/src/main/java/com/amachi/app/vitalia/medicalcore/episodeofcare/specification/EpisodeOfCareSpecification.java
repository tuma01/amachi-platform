package com.amachi.app.vitalia.medicalcore.episodeofcare.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.episodeofcare.dto.search.EpisodeOfCareSearchDto;
import com.amachi.app.vitalia.medicalcore.episodeofcare.entity.EpisodeOfCare;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class EpisodeOfCareSpecification extends BaseSpecification<EpisodeOfCare> {

    private final EpisodeOfCareSearchDto criteria;

    public EpisodeOfCareSpecification(EpisodeOfCareSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<EpisodeOfCare> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getPatientId() != null)
            predicates.add(cb.equal(root.get("patient").get("id"), criteria.getPatientId()));

        if (criteria.getManagingDoctorId() != null)
            predicates.add(cb.equal(root.get("managingDoctor").get("id"), criteria.getManagingDoctorId()));

        if (criteria.getStatus() != null)
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
