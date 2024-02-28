package com.free.swd_392.repository.product;

import com.free.swd_392.entity.product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ProductRepository extends
        JpaRepository<ProductEntity, Long>,
        JpaSpecificationExecutor<ProductEntity> {

    @Query(value = """
                SELECT p
                FROM ProductEntity p
                    LEFT JOIN FETCH ProductConfigEntity pc ON pc.product.id = p.id
                    LEFT JOIN FETCH ProductVariantEntity pv ON  pv.config.id = pc.id
                WHERE p.id = :id
            """)
    Optional<ProductEntity> findByIdFetchConfigVariant(@Param("id") Long id);
}
