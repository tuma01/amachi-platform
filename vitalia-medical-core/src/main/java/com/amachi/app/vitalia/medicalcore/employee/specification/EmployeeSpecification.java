package com.amachi.app.vitalia.medicalcore.employee.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.vitalia.medicalcore.employee.dto.search.EmployeeSearchDto;
import com.amachi.app.vitalia.medicalcore.employee.entity.Employee;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification extends BaseSpecification<Employee> {

    private final EmployeeSearchDto criteria;

    public EmployeeSpecification(EmployeeSearchDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>(buildBasePredicates(root, cb));

        if (criteria == null) return cb.and(predicates.toArray(new Predicate[0]));

        if (criteria.getEmployeeCode() != null && !criteria.getEmployeeCode().isBlank())
            predicates.add(cb.like(cb.lower(root.get("employeeCode")), "%" + criteria.getEmployeeCode().toLowerCase() + "%"));

        if (criteria.getEmployeeType() != null)
            predicates.add(cb.equal(root.get("employeeType"), criteria.getEmployeeType()));

        if (criteria.getEmployeeStatus() != null)
            predicates.add(cb.equal(root.get("employeeStatus"), criteria.getEmployeeStatus()));

        if (criteria.getDepartmentUnitId() != null)
            predicates.add(cb.equal(root.get("departmentUnit").get("id"), criteria.getDepartmentUnitId()));

        if (criteria.getWorkShift() != null)
            predicates.add(cb.equal(root.get("workShift"), criteria.getWorkShift()));

        if (criteria.getName() != null && !criteria.getName().isBlank()) {
            String pattern = "%" + criteria.getName().toLowerCase() + "%";
            predicates.add(cb.or(
                cb.like(cb.lower(root.join("person").get("firstName")), pattern),
                cb.like(cb.lower(root.join("person").get("lastName")), pattern)
            ));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
