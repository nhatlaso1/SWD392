package com.free.swd_392.entity.order;

import com.free.swd_392.entity.product.ProductEntity;
import com.free.swd_392.shared.constant.TableName;
import com.free.swd_392.shared.model.order.OrderExtraVariant;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = TableName.ORDER_ITEM)
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50)
    private String productName;
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal discount = BigDecimal.ZERO;
    private Integer quantity;
    @Type(JsonType.class)
    @Column(columnDefinition = "JSON")
    private List<OrderExtraVariant> extraVariants;
    @Column(length = 200)
    private String note;
    @Column(name = "product_id")
    private Long productId;

    @ToString.Exclude
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            targetEntity = OrderEntity.class,
            optional = false
    )
    @JoinColumn(
            name = "order_id",
            foreignKey = @ForeignKey(name = "fk_order_item_order_id")
    )
    private OrderEntity order;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ProductEntity.class, optional = false)
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "fk_order_item_product_id"),
            insertable = false,
            updatable = false
    )
    private ProductEntity product;

    public BigDecimal subTotalItem() {
        return (price.subtract(discount)).multiply(BigDecimal.valueOf(quantity));
    }

    public String productFullName() {
        return productName + " - " + Objects.requireNonNullElse(extraVariants, Collections.<OrderExtraVariant>emptyList())
                .stream()
                .flatMap(exv -> exv.getVariants().stream())
                .collect(Collectors.joining(", "));
    }
}
