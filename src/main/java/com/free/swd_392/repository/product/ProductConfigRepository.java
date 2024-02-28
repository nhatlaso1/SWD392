package com.free.swd_392.repository.product;

import com.free.swd_392.entity.product.ProductConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ProductConfigRepository extends
        JpaRepository<ProductConfigEntity, Long>,
        JpaSpecificationExecutor<ProductConfigEntity> {
}
