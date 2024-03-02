package com.free.swd_392.entity.product;

import com.free.swd_392.entity.audit.Audit;
import com.free.swd_392.shared.constant.TableName;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.*;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TableName.PRODUCT_SKU)
public class SkuEntity extends Audit<String> {

    @Id
    @UuidGenerator
    private UUID id;
    private BigDecimal price;
    private Integer quantity;
    @Column(length = 2083)
    private String image;
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "fk_sku_product_id"),
            insertable = false,
            updatable = false
    )
    private ProductEntity product;

    @ToString.Exclude
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            orphanRemoval = true,
            targetEntity = SkuConfigEntity.class,
            mappedBy = "sku"
    )
    @Fetch(FetchMode.SUBSELECT)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<SkuConfigEntity> configs;

    public void setVariants(List<ProductVariantEntity> variants) {
        if (CollectionUtils.isEmpty(variants)) {
            return;
        }
        if (configs == null) {
            configs = new ArrayList<>();
        } else {
            configs.clear();
        }
        variants.stream()
                .map(v -> new SkuConfigEntity(this, v))
                .forEach(configs::add);
    }

    public List<Long> getVariantIds() {
        if (CollectionUtils.isEmpty(configs)) {
            return Collections.emptyList();
        }
        return configs.stream()
                .mapToLong(c -> c.getId().getVariantId())
                .boxed()
                .toList();
    }
}
