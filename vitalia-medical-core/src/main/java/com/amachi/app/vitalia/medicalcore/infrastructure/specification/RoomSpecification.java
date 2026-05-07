package com.amachi.app.vitalia.medicalcore.infrastructure.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.search.RoomSearchDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Room;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class RoomSpecification extends BaseSpecification<Room> {

    private transient RoomSearchDto criteria;

    @Override
    public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getId() != null) {
            predicates.add(cb.equal(root.get("id"), criteria.getId()));
        }

        if (criteria.getUnitId() != null) {
            predicates.add(cb.equal(root.get("unit").get("id"), criteria.getUnitId()));
        }

        if (criteria.getRoomNumber() != null && !criteria.getRoomNumber().isBlank()) {
            predicates.add(cb.like(cb.upper(root.get("roomNumber")), "%" + criteria.getRoomNumber().toUpperCase() + "%"));
        }

        if (criteria.getRoomType() != null) {
            predicates.add(cb.equal(root.get("roomType"), criteria.getRoomType()));
        }

        if (criteria.getCleaningStatus() != null) {
            predicates.add(cb.equal(root.get("cleaningStatus"), criteria.getCleaningStatus()));
        }

        if (criteria.getPrivateRoom() != null) {
            predicates.add(cb.equal(root.get("privateRoom"), criteria.getPrivateRoom()));
        }

        if (criteria.getActive() != null) {
            predicates.add(cb.equal(root.get("active"), criteria.getActive()));
        }

        if (criteria.getBlockCode() != null && !criteria.getBlockCode().isBlank()) {
            predicates.add(cb.equal(cb.upper(root.get("blockCode")), criteria.getBlockCode().toUpperCase()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
