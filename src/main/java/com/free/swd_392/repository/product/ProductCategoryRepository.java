package com.free.swd_392.repository.product;

import com.free.swd_392.dto.product.ProductCategoryInfo;
import com.free.swd_392.entity.product.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ProductCategoryRepository extends
        JpaRepository<ProductCategoryEntity, Long>,
        JpaSpecificationExecutor<ProductCategoryEntity> {

    int countByParentId(Long parentId);

    @Query(name = "findAllCategoryActivatingRecursive", nativeQuery = true)
    List<ProductCategoryInfo> findAllCategoryActivatingRecursive();

    @Modifying
    @Query(value = """
            UPDATE ProductCategoryEntity pc
            SET pc.ordering = pc.ordering + :coefficient
            WHERE (:parentId IS NULL OR pc.parentId = :parentId)
                AND :start <= pc.ordering
                AND :end >= pc.ordering
            """)
    void updateOrdering(@Param("parentId") Long parentId,
                        @Param("start") Integer start,
                        @Param("end") Integer end,
                        @Param("coefficient") Integer coefficient);
}
