package com.free.swd_392.entity.order;

import com.free.swd_392.entity.audit.Audit;
import com.free.swd_392.entity.location.LocationEntity;
import com.free.swd_392.entity.merchant.MerchantEntity;
import com.free.swd_392.entity.payment.PaymentEntity;
import com.free.swd_392.entity.user.UserAddressEntity;
import com.free.swd_392.entity.user.UserEntity;
import com.free.swd_392.enums.OrderStatus;
import com.free.swd_392.enums.order.PaymentMethodProvider;
import com.free.swd_392.shared.constant.TableName;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.jpa.domain.AbstractAuditable_.CREATED_BY;

@Getter
@Setter
@ToString
@FieldNameConstants
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
    @Column(name = "province_id", nullable = false)
    private Long provinceId;
    @Transient
    private String provinceName;
    @NotAudited
    @Column(name = "district_id", nullable = false)
    private Long districtId;
    @NotAudited
    @Column(name = "ward_id", nullable = false)
    private Long wardId;
    @NotAudited
    @Column(length = 150, nullable = false)
    private String addressDetails;
    @NotAudited
    @Column(columnDefinition = "DECIMAL(38,2) DEFAULT 0.0", nullable = false)
    private BigDecimal subTotal = BigDecimal.ZERO;
    @NotAudited
    @Column(columnDefinition = "DECIMAL(38,2) DEFAULT 0.0", nullable = false)
    private BigDecimal shippingCharge = BigDecimal.ZERO;
    @NotAudited
    @Column(columnDefinition = "DECIMAL(38,2) DEFAULT 0.0", nullable = false)
    private BigDecimal discount = BigDecimal.ZERO;
    @NotAudited
    @Column(columnDefinition = "DECIMAL(38,2) DEFAULT 0.0", nullable = false)
    private BigDecimal total = BigDecimal.ZERO;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private PaymentMethodProvider paymentMethod;
    @Column(length = 32, nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @NotAudited
    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;
    @NotAudited
    @Column(name = "payment_id")
    private UUID paymentId;

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
    @NotAudited
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = MerchantEntity.class, optional = false)
    @JoinColumn(
            name = "merchant_id",
            foreignKey = @ForeignKey(name = "fk_order_merchant_id"),
            insertable = false,
            updatable = false
    )
    private MerchantEntity merchant;
    @NotAudited
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = PaymentEntity.class)
    @JoinColumn(
            name = "payment_id",
            foreignKey = @ForeignKey(name = "fk_order_payment_id"),
            insertable = false,
            updatable = false
    )
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private PaymentEntity payment;
    @NotAudited
    @ToString.Exclude
    @OneToMany(mappedBy = "order", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, targetEntity = OrderItemEntity.class)
    @Fetch(FetchMode.SUBSELECT)
    private List<OrderItemEntity> orderItems = new ArrayList<>();
    @NotAudited
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = LocationEntity.class)
    @JoinColumn(
            name = "province_id",
            foreignKey = @ForeignKey(name = "fk_order_province_location_id"),
            insertable = false,
            updatable = false
    )
    private LocationEntity province;
    @NotAudited
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = LocationEntity.class)
    @JoinColumn(
            name = "district_id",
            foreignKey = @ForeignKey(name = "fk_order_district_location_id"),
            insertable = false,
            updatable = false
    )
    private LocationEntity district;
    @NotAudited
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = LocationEntity.class)
    @JoinColumn(
            name = "ward_id",
            foreignKey = @ForeignKey(name = "fk_order_ward_location_id"),
            insertable = false,
            updatable = false
    )
    private LocationEntity ward;

    public void mapUserAddress(UserAddressEntity address) {
        this.receiverFullName = address.getReceiverFullName();
        this.phone = address.getPhone();
        this.provinceId = address.getProvinceId();
        this.districtId = address.getDistrictId();
        this.wardId = address.getWardId();
        this.addressDetails = address.getAddressDetails();
    }

    public void addOrderItem(OrderItemEntity orderItem) {
        if (CollectionUtils.isEmpty(orderItems)) {
            orderItems = new ArrayList<>();
        }
        if (orderItem.getDiscount() != null) {
            discount = discount.add(orderItem.getDiscount());
        }
        var itemPrice = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
        subTotal = subTotal.add(itemPrice);
        total = subTotal.add(shippingCharge).subtract(discount);
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public String addressLine() {
        return String.format("%s, %s, %s, %s",
                addressDetails,
                ward.getName(),
                district.getName(),
                province.getName()
        );
    }
}
