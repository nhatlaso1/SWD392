package com.free.swd_392.dto.merchant.request.filter;

import com.free.swd_392.core.model.IPageFilter;
import com.free.swd_392.entity.merchant.MerchantEntity;
import com.free.swd_392.enums.MerchantStatus;
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

import static com.free.swd_392.shared.constant.SqlFunctionName.FULL_TEXT_SEARCH;

@Data
public class SystemMerchantInfoPageFilter implements IPageFilter, Specification<MerchantEntity> {

    private Pageable pageable;
    private String name;
    private String phone;
    private String representativeEmail;
    private MerchantStatus status;

    @Override
    public Predicate toPredicate(@NonNull Root<MerchantEntity> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(getName())) {
            predicates.add(cb.isTrue(cb.function(FULL_TEXT_SEARCH, Boolean.class, root.get(MerchantEntity.Fields.name), cb.literal(getName()))));
        }

        if (StringUtils.isNotBlank(getPhone())) {
            predicates.add(cb.like(root.get(MerchantEntity.Fields.phone), getPhone() + "%"));
        }

        if (StringUtils.isNotBlank(getRepresentativeEmail())) {
            predicates.add(cb.like(cb.lower(root.get(MerchantEntity.Fields.representativeEmail)), "%" + getRepresentativeEmail().toLowerCase() + "%"));
        }

        if (getStatus() != null) {
            predicates.add(cb.equal(root.get(MerchantEntity.Fields.status), getStatus()));
        }

        root.fetch(MerchantEntity.Fields.province);
        root.fetch(MerchantEntity.Fields.district);
        root.fetch(MerchantEntity.Fields.ward);

        return cb.and(predicates.toArray(Predicate[]::new));
    }
}
