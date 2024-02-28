package com.free.swd_392.dto.product.request.filter;

import com.free.swd_392.core.model.IFilter;
import com.free.swd_392.entity.product.SkuEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class SystemSkuListFilter implements IFilter, Specification<SkuEntity> {

    @Override
    public Predicate toPredicate(Root<SkuEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
