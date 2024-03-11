package com.free.swd_392.entity.payment;

import com.free.swd_392.config.client.paypal.response.CreateOrderResponse;
import com.free.swd_392.entity.audit.Audit;
import com.free.swd_392.entity.merchant.MerchantEntity;
import com.free.swd_392.entity.order.OrderEntity;
import com.free.swd_392.enums.PaymentStatus;
import com.free.swd_392.enums.PaymentType;
import com.free.swd_392.shared.constant.TableName;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TableName.PAYMENT)
public class PaymentEntity extends Audit<String> {

    @Id
    private UUID id;
    private BigDecimal amount;
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    private String refTransactionId;
    @JdbcTypeCode(SqlTypes.JSON)
    private CreateOrderResponse paymentData;
    @Column(name = "merchant_id")
    private Long merchantId;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = MerchantEntity.class)
    @JoinColumn(
            name = "merchant_id",
            foreignKey = @ForeignKey(name = "fk_payment_merchant_id"),
            insertable = false,
            updatable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MerchantEntity merchant;

    @OneToMany(mappedBy = "payment")
    @Fetch(FetchMode.SUBSELECT)
    private List<OrderEntity> orders;
}
