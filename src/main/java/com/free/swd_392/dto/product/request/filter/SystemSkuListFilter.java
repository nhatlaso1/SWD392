package com.free.swd_392.dto.product.request.filter;

import com.free.swd_392.core.model.IFilter;
import com.free.swd_392.entity.product.SkuEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class SystemSkuListFilter implements IFilter, Specification<SkuEntity> {

    @NotNull
    private Long productId;

    @Override
    public Predicate toPredicate(Root<SkuEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get(SkuEntity.Fields.productId), productId);
    }
}
