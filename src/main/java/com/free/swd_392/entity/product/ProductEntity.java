package com.free.swd_392.entity.product;

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

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = TableName.PRODUCT)
public class ProductEntity extends Audit<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 200)
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
