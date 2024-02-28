package com.free.swd_392.dto.product.request.filter;

import com.free.swd_392.core.model.IPageFilter;
import com.free.swd_392.entity.product.ProductEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class SystemProductPageFilter implements IPageFilter, Specification<ProductEntity> {

    private Pageable pageable;
    private String name;
    private Long categoryId;

    @Override
    public Predicate toPredicate(@NonNull Root<ProductEntity> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(name)) {
            predicates.add(cb.like(cb.lower(root.get(ProductEntity.Fields.name)), "%" + name.toLowerCase() + "%"));
        }

        if (categoryId != null) {
            predicates.add(cb.equal(root.get(ProductEntity.Fields.categoryId), categoryId));
        }

        root.fetch(ProductEntity.Fields.category);

        return cb.and(predicates.toArray(Predicate[]::new));
    }
}
