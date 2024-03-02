package com.free.swd_392.dto.user.request;

import com.free.swd_392.core.model.IFilter;
import com.free.swd_392.entity.user.UserAddressEntity;
import com.free.swd_392.shared.utils.JwtUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import static com.free.swd_392.entity.audit.Audit.Fields;

@Data
public class AppUserAddressFilter implements IFilter, Specification<UserAddressEntity> {

    @Override
    public Predicate toPredicate(@NonNull Root<UserAddressEntity> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        root.fetch(UserAddressEntity.Fields.province);
        root.fetch(UserAddressEntity.Fields.district);
        root.fetch(UserAddressEntity.Fields.ward);
        return cb.equal(root.get(Fields.createdBy), JwtUtils.getUserId());
    }
}
