package com.free.swd_392.repository.product;

import com.free.swd_392.entity.product.SkuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface SkuRepository extends
        JpaRepository<SkuEntity, UUID>,
        JpaSpecificationExecutor<SkuEntity> {

    List<SkuEntity> getAllByProductId(Long productId);

    boolean existsByIdAndProductMerchantId(UUID id, Long merchantId);
}
