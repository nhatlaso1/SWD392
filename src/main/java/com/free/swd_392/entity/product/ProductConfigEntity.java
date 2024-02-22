package com.free.swd_392.entity.product;

import com.free.swd_392.enums.ProductConfigChoice;
import com.free.swd_392.shared.constant.TableName;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TableName.PRODUCT_CONFIG)
public class ProductConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private ProductConfigChoice choiceKind;
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isRequired = false;
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ProductEntity.class, optional = false)
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "fk_product_config_product_id"),
            insertable = false,
            updatable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductEntity product;

    @ToString.Exclude
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = ProductVariantEntity.class)
    @JoinColumn(
            name = "product_config_id",
            foreignKey = @ForeignKey(name = "fk_product_config_product_variant_id")
    )
    @OrderBy(value = "ordering")
    @Fetch(FetchMode.SUBSELECT)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ProductVariantEntity> variants;
}
