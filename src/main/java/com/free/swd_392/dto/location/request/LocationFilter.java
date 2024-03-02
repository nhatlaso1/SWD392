package com.free.swd_392.dto.location.request;

import com.free.swd_392.core.model.IFilter;
import com.free.swd_392.entity.location.LocationEntity;
import com.free.swd_392.enums.LocationKind;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

import static com.free.swd_392.shared.constant.SqlFunctionName.FULL_TEXT_SEARCH;

@Data
public class LocationFilter implements IFilter, Specification<LocationEntity> {

    private String name;
    private Long parentId;
    @NotNull
    private LocationKind kind;

    @Override
    public Predicate toPredicate(@NonNull Root<LocationEntity> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
        if (getName() != null) {
            predicates.add(cb.isTrue(cb.function(FULL_TEXT_SEARCH, Boolean.class, root.get(LocationEntity.Fields.name), cb.literal(getName()))));
        }
        if (getParentId() != null) {
            predicates.add(cb.equal(root.get(LocationEntity.Fields.parentId), getParentId()));
        } else {
            predicates.add(cb.isNull(root.get("parent")));
        }
        if (getKind() != null) {
            predicates.add(cb.equal(root.get(LocationEntity.Fields.kind), getKind()));
        }
        return cb.and(predicates.toArray(Predicate[]::new));
    }
}
