package com.free.swd_392.entity.product;

import com.free.swd_392.shared.constant.TableName;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TableName.PRODUCT_SKU_CONFIG)
@IdClass(SkuConfigId.class)
public class SkuConfigEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 3L;

    @Id
    @Column(name = "product_config_id", nullable = false)
    private Long configId;
    @Id
    @Column(name = "product_variant_id", nullable = false)
    private Long variantId;
    @Column(name = "sku_id")
    private UUID skuId;

    @ToString.Exclude
    @MapsId("configId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_config_id",
            foreignKey = @ForeignKey(name = "fk_sku_config_product_config_id"),
            insertable = false,
            updatable = false
    )
    private ProductConfigEntity pConfig;
    @ToString.Exclude
    @MapsId("variantId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_variant_id",
            foreignKey = @ForeignKey(name = "fk_sku_config_product_variant_id"),
            insertable = false,
            updatable = false
    )
    private ProductVariantEntity variant;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "sku_id",
            insertable = false,
            updatable = false
    )
    private SkuEntity sku;
}
