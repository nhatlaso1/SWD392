package com.free.swd_392.dto.user.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

import java.util.ArrayList;
import java.util.List;

@Data
public class SystemUserPageFilter implements IPageFilter, Specification<UserEntity> {

    @JsonIgnore
    private Pageable pageable;

    private String email;
    private String phone;

    @Override
    public Predicate toPredicate(@NonNull Root<UserEntity> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(getEmail())) {
            predicates.add(cb.equal(cb.lower(root.get(UserEntity.Fields.email)), "%" + getEmail().toLowerCase() + "%"));
        }

        if (StringUtils.isNotBlank(getPhone())) {
            predicates.add(cb.equal(cb.lower(root.get(UserEntity.Fields.phone)), "%" + getPhone().toLowerCase() + "%"));
        }

        root.fetch(UserEntity.Fields.role);

        return cb.and(predicates.toArray(Predicate[]::new));
    }
}
