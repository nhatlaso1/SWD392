package com.free.swd_392.dto.user.request;

import com.free.swd_392.core.model.IPageFilter;
import com.free.swd_392.entity.user.UserEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Data
public class SystemUserPageFilter implements IPageFilter, Specification<UserEntity> {

    private Pageable pageable;

    private String email;
    private String phone;

    @Override
    public Predicate toPredicate(@NonNull Root<UserEntity> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        Predicate[] predicates = new Predicate[2];

        if (StringUtils.isNotBlank(getEmail())) {
            predicates[0] = cb.equal(cb.lower(root.get(UserEntity.Fields.email)), "%" + getEmail().toLowerCase() + "%");
        }

        if (StringUtils.isNotBlank(getPhone())) {
            predicates[1] = cb.equal(cb.lower(root.get(UserEntity.Fields.phone)), "%" + getPhone().toLowerCase() + "%");
        }

        return cb.and(predicates);
    }
}
