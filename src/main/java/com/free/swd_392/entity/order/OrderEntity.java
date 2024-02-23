package com.free.swd_392.entity.order;

import com.free.swd_392.entity.audit.Audit;
import com.free.swd_392.entity.user.UserEntity;
import com.free.swd_392.enums.OrderStatus;
import com.free.swd_392.enums.order.PaymentMethod;
import com.free.swd_392.shared.constant.TableName;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractAuditable_.CREATED_BY;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TableName.ORDER)
@Audited
public class OrderEntity extends Audit<String> {

    @Id
    @UuidGenerator
    private UUID id;
    @NotAudited
    @Column(length = 50)
    private String receiverFullName;
    @NotAudited
    @Column(length = 10)
    private String phone;
    @NotAudited
    private Long provinceId;
    @NotAudited
    private Long districtId;
    @NotAudited
    private Long wardId;
    @NotAudited
    @Column(length = 150)
    private String addressDetails;
    @NotAudited
    private BigDecimal subTotal;
    @NotAudited
    private Double shippingCharge;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @NotAudited
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class, optional = false)
    @JoinColumn(
            name = CREATED_BY,
            foreignKey = @ForeignKey(name = "fk_order_user_id"),
            insertable = false,
            updatable = false
    )
    private UserEntity user;
}
