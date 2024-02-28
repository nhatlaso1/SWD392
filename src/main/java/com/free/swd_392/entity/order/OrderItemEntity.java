package com.free.swd_392.entity.order;

import com.free.swd_392.dto.product.ProductConfigInfo;
import com.free.swd_392.entity.product.ProductEntity;
import com.free.swd_392.shared.constant.TableName;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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
    private BigDecimal price;
    private BigDecimal discount;
    private Integer quantity;
    @Type(JsonType.class)
    @Column(columnDefinition = "JSON")
    private List<ProductConfigInfo> extraVariant;
    @Column(length = 200)
    private String note;
    @Column(name = "order_id", updatable = false, nullable = false)
    private UUID orderId;
    @Column(name = "product_id", updatable = false, nullable = false)
    private Long productId;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = OrderEntity.class, optional = false)
    @JoinColumn(
            name = "order_id",
            foreignKey = @ForeignKey(name = "fk_order_item_order_id"),
            insertable = false,
            updatable = false
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
}
