package com.free.swd_392.repository.product;

import com.free.swd_392.entity.product.ProductVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface ProductVariantRepository extends
        JpaRepository<ProductVariantEntity, Long>,
        JpaSpecificationExecutor<ProductVariantEntity> {

    @Query(value = """
                SELECT pv
                FROM SkuConfigEntity sc
                    INNER JOIN ProductVariantEntity pv ON pv.id = sc.id.variantId
                    INNER JOIN FETCH pv.config
                WHERE sc.id.skuId = :skuId
            """)
    List<ProductVariantEntity> findVariantsBySku(@Param("skuId") UUID skuId);
}
