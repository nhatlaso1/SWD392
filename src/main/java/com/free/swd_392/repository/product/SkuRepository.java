package com.free.swd_392.repository.product;

import com.free.swd_392.entity.product.SkuEntity;
import org.springframework.data.jpa.repository.EntityGraph;
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
public interface SkuRepository extends
        JpaRepository<SkuEntity, UUID>,
        JpaSpecificationExecutor<SkuEntity> {

    List<SkuEntity> getAllByProductId(Long productId);

    @Query(value = """
                SELECT sku
                FROM SkuEntity sku
                    LEFT JOIN SkuConfigEntity sc ON sku.id = sc.sku.id
                    LEFT JOIN ProductVariantEntity pv ON sc.variant.id = pv.id
                WHERE pv.isSoldOut = FALSE
                    AND sku.productId = :productId
            """)
    List<SkuEntity> getAllByProductIdAndVariantsNotSoldOut(@Param("productId") Long productId);

    boolean existsByIdAndProductMerchantId(UUID id, Long merchantId);

    @Query(value = """
                SELECT sku.id
                FROM SkuEntity sku
                WHERE sku.id IN :ids
            """)
    List<UUID> findByIdIn(@Param("ids") List<UUID> ids);

    @Query(value = """
                SELECT sku
                FROM SkuEntity sku
                WHERE sku.id IN :ids
            """)
    @EntityGraph(attributePaths = {"product", "product.merchant"})
    List<SkuEntity> findByIdsFetchJoinProduct(@Param("ids") List<UUID> ids);
}
