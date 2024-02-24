package com.free.swd_392.entity.product;

import com.free.swd_392.dto.product.ProductCategoryInfo;
import com.free.swd_392.entity.audit.Audit;
import com.free.swd_392.enums.ProductCategoryStatus;
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
@Table(name = TableName.PRODUCT_CATEGORY)
@NamedNativeQuery(
        name = "findAllCategoryActivatingRecursive",
        query = """
                WITH RECURSIVE cte AS (
                    SELECT id, description, icon, name, ordering, parent_id, status
                    FROM auction_product_category pc
                    WHERE status = 'ACTIVE' AND pc.parent_id IS NULL
                    UNION ALL
                    SELECT pc.id, pc.description, pc.icon, pc.name, pc.ordering, pc.parent_id, pc.status
                    FROM auction_product_category pc
                            INNER JOIN cte on pc.parent_id = cte.id
                    WHERE pc.status = 'ACTIVE'
                )
                SELECT id, description, icon, name, ordering, parent_id, status
                FROM cte
                """,
        resultSetMapping = "findAllCategoryActivatingRecursiveMapper"
)
@SqlResultSetMapping(
        name = "findAllCategoryActivatingRecursiveMapper",
        classes = @ConstructorResult(
                targetClass = ProductCategoryInfo.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "icon", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "ordering", type = Integer.class),
                        @ColumnResult(name = "parent_id", type = Long.class),
                        @ColumnResult(name = "status", type = String.class),
                }
        )
)
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
    private ProductCategoryStatus status;
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

    @OneToMany(mappedBy = "parent")
    @Fetch(FetchMode.SUBSELECT)
    private List<ProductCategoryEntity> children;
}
