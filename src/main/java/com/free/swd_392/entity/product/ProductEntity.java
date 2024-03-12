package com.free.swd_392.entity.product;

import com.free.swd_392.dto.product.ProductInfo;
import com.free.swd_392.entity.audit.Audit;
import com.free.swd_392.entity.merchant.MerchantEntity;
import com.free.swd_392.enums.ProductStatus;
import com.free.swd_392.shared.constant.TableName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = TableName.PRODUCT)
@NamedNativeQuery(
        name = "findProductFilter",
        query = """
                    SELECT DISTINCT (p.id), p.name, p.image, p.status, p.category_id,
                            c.name as category_name, sku_agg.min_price AS min_price, sku_agg.max_price AS max_price
                    FROM auction_product p
                        LEFT JOIN auction_product_category c ON p.category_id = c.id
                        LEFT JOIN (
                            SELECT sku.product_id, COUNT(sku.id) AS skuNum, MIN(sku.price) as min_price, MAX(sku.price) as max_price
                            FROM auction_product_sku sku
                            WHERE (:fromPrice IS NULL OR sku.price >= :fromPrice)
                                AND (:toPrice IS NULL OR sku.price <= :toPrice)
                            GROUP BY sku.product_id
                        ) AS sku_agg ON sku_agg.product_id = p.id
                        LEFT JOIN (
                            SELECT pc.id, pc.product_id, SUM(IF(pv.is_sold_out = FALSE, 1, 0)) AS variant_available
                            FROM auction_product_config pc
                                LEFT JOIN auction_product_variant pv ON pc.id = pv.product_config_id
                            GROUP BY pc.id
                        ) AS pc ON pc.product_id = p.id
                    WHERE p.status = 'ACTIVE'
                        AND (:name IS NULL OR MATCH(p.name) AGAINST(:name))
                        AND (COALESCE(:categoryIds) IS NULL OR p.category_id IN (:categoryIds))
                        AND pc.variant_available > 0
                        AND sku_agg.skuNum > 0
                    GROUP BY p.id
                """,
        resultSetMapping = "findProductFilterMapper"
)
@NamedNativeQuery(
        name = "findProductFilter.count",
        query = """
                    SELECT COUNT(*) AS cnt
                    FROM auction_product p
                        LEFT JOIN auction_product_category c ON p.category_id = c.id
                        LEFT JOIN (
                            SELECT sku.product_id, COUNT(sku.id) AS skuNum
                            FROM auction_product_sku sku
                            WHERE (:fromPrice IS NULL OR sku.price >= :fromPrice)
                                AND (:toPrice IS NULL OR sku.price <= :toPrice)
                            GROUP BY sku.product_id
                        ) AS sku_agg ON sku_agg.product_id = p.id
                        LEFT JOIN (
                            SELECT pc.id, pc.product_id, SUM(IF(pv.is_sold_out = FALSE, 1, 0)) AS variant_available
                            FROM auction_product_config pc
                                LEFT JOIN auction_product_variant pv ON pc.id = pv.product_config_id
                            GROUP BY pc.id
                        ) AS pc ON pc.product_id = p.id
                    WHERE p.status = 'ACTIVE'
                        AND (:name IS NULL OR MATCH(p.name) AGAINST(:name))
                        AND (COALESCE(:categoryIds) IS NULL OR p.category_id IN (:categoryIds))
                        AND pc.variant_available > 0
                        AND sku_agg.skuNum > 0
                    GROUP BY p.id
                """,
        resultSetMapping = "findProductFilterMapper.count"
)
@SqlResultSetMapping(
        name = "findProductFilterMapper",
        classes = @ConstructorResult(
                targetClass = ProductInfo.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "image", type = String.class),
                        @ColumnResult(name = "status", type = ProductStatus.class),
                        @ColumnResult(name = "category_id", type = Long.class),
                        @ColumnResult(name = "category_name", type = String.class),
                        @ColumnResult(name = "min_price", type = BigDecimal.class),
                        @ColumnResult(name = "max_price", type = BigDecimal.class)
                }
        )
)
@SqlResultSetMapping(name = "findProductFilterMapper.count", columns = @ColumnResult(name = "cnt"))
public class ProductEntity extends Audit<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(200) NOT NULL, FULLTEXT KEY nameFullText(name)")
    private String name;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    @Column(length = 2083)
    private String image;
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isSoldOut = false;
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ProductCategoryEntity.class, optional = false)
    @JoinColumn(
            name = "category_id",
            foreignKey = @ForeignKey(name = "fk_product_category_id"),
            insertable = false,
            updatable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductCategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = MerchantEntity.class, optional = false)
    @JoinColumn(
            name = "merchant_id",
            foreignKey = @ForeignKey(name = "fk_product_merchant_id"),
            insertable = false,
            updatable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MerchantEntity merchant;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = ProductConfigEntity.class)
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "fk_product_config_product_id")
    )
    @Fetch(FetchMode.SUBSELECT)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProductConfigEntity> productConfigs;

    @OneToMany(mappedBy = "product")
    @Fetch(FetchMode.SUBSELECT)
    private List<SkuEntity> skus;
}
