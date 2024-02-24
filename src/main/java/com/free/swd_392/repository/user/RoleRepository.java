package com.free.swd_392.repository.user;

import com.free.swd_392.entity.product.ProductCategoryEntity;
import com.free.swd_392.entity.user.RoleEntity;
import com.free.swd_392.enums.RoleKind;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public interface RoleRepository extends
        JpaRepository<RoleEntity, UUID>,
        JpaSpecificationExecutor<ProductCategoryEntity> {

    Optional<RoleEntity> findFirstByKind(RoleKind kind);
}
