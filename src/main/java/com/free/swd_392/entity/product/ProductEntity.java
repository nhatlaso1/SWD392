package com.free.swd_392.entity.product;

import com.free.swd_392.entity.audit.Audit;
import com.free.swd_392.enums.ProductStatus;
import com.free.swd_392.shared.constant.TableName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ProductCategoryEntity.class, optional = false)
    @JoinColumn(
            name = "category_id",
            foreignKey = @ForeignKey(name = "fk_product_category_id"),
            insertable = false,
            updatable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductCategoryEntity category;
}
