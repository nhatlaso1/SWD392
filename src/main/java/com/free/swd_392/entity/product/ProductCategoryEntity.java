package com.free.swd_392.entity.product;

import com.free.swd_392.entity.audit.Audit;
import com.free.swd_392.enums.CategoryStatus;
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
@Table(name = TableName.PRODUCT_CATEGORY)
public class ProductCategoryEntity extends Audit<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 200)
    private String name;
    private Integer ordering;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String icon;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private CategoryStatus status;
    @Column(name = "parent_id")
    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ProductCategoryEntity.class)
    @JoinColumn(
            name = "parent_id",
            foreignKey = @ForeignKey(name = "fk_product_category_parent_id"),
            insertable = false,
            updatable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductCategoryEntity parent;
}
