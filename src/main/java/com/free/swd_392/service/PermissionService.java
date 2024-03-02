package com.free.swd_392.service;

import com.free.swd_392.entity.audit.Audit;
import com.free.swd_392.shared.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PermissionService {

    public <E extends Audit<?>> boolean isOwner(E entity) {
        if (entity == null) {
            return false;
        }
        return Objects.equals(JwtUtils.getUserId(), entity.getCreatedBy());
    }

    public <A extends Serializable, E extends Audit<String>, R extends JpaRepository<E, A>> boolean isOwner(A id, R repository) {
        if (id == null || repository == null) {
            return false;
        }
        return repository.findById(id)
                .filter(this::isOwner)
                .isPresent();
    }
}
