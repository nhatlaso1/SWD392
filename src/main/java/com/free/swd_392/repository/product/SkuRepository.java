package com.free.swd_392.repository.product;

import com.free.swd_392.entity.product.SkuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional
public interface SkuRepository extends
        JpaRepository<SkuEntity, UUID>,
        JpaSpecificationExecutor<SkuEntity> {
}
