package com.free.swd_392.dto.product.request.filter;

import com.free.swd_392.core.model.IFilter;
import com.free.swd_392.entity.product.ProductCategoryEntity;
import com.free.swd_392.enums.ProductCategoryStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductCategoryFilter implements IFilter, Specification<ProductCategoryEntity> {

    private Long parentId;
    private ProductCategoryStatus status;

    @Override
    public Predicate toPredicate(@NonNull Root<ProductCategoryEntity> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (getParentId() != null) {
            predicates.add(cb.equal(root.get(ProductCategoryEntity.Fields.parentId), getParentId()));
        } else {
            predicates.add(cb.isNull(root.get(ProductCategoryEntity.Fields.parentId)));
        }

        if (getStatus() != null) {
            predicates.add(cb.equal(root.get(ProductCategoryEntity.Fields.status), getStatus()));
        }

        return cb.and(predicates.toArray(Predicate[]::new));
    }
}
