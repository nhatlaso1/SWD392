package com.free.swd_392.entity.product;

import com.free.swd_392.shared.constant.TableName;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TableName.PRODUCT_SKU_CONFIG)
public class SkuConfigEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 3L;

    @Id
    @EmbeddedId
    private SkuConfigId id;

    @ToString.Exclude
    @MapsId("variantId")
    @ManyToOne(fetch = LAZY, cascade = {MERGE, PERSIST})
    @JoinColumn(
            name = "variant_id",
            foreignKey = @ForeignKey(name = "fk_sku_config_variant_id"),
            insertable = false,
            updatable = false
    )
    private ProductVariantEntity variant;
    @ToString.Exclude
    @MapsId("skuId")
    @ManyToOne(fetch = LAZY, cascade = {MERGE, PERSIST})
    @JoinColumn(name = "sku_id",
            insertable = false,
            foreignKey = @ForeignKey(name = "fk_sku_config_sku_id"),
            updatable = false)
    private SkuEntity sku;

    public SkuConfigEntity(SkuEntity sku, ProductVariantEntity variant) {
        setSku(sku);
        setVariant(variant);
        if (sku != null && sku.getId() != null && variant != null && variant.getId() != null) {
            setId(new SkuConfigId(sku.getId(), variant.getId()));
        }
    }
}
