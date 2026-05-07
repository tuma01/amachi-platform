package com.amachi.app.core.management.avatar.specification;

import com.amachi.app.core.common.specification.BaseSpecification;
import com.amachi.app.core.management.avatar.dto.search.AvatarSearchDto;
import com.amachi.app.core.management.avatar.entity.Avatar;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

public class AvatarSpecification extends BaseSpecification<Avatar> {

    private final AvatarSearchDto filter;

    public AvatarSpecification(AvatarSearchDto filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<Avatar> root,
                                 @Nullable CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = buildBasePredicates(root, cb);

        if (filter.getUserId() != null) {
            predicates.add(cb.equal(root.get("user").get("id"), filter.getUserId()));
        }
        if (filter.getMimeType() != null && !filter.getMimeType().isBlank()) {
            predicates.add(cb.equal(root.get("mimeType"), filter.getMimeType()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
