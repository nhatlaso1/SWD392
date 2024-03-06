package com.free.swd_392.entity.payment;

import com.free.swd_392.entity.audit.Audit;
import com.free.swd_392.entity.merchant.MerchantEntity;
import com.free.swd_392.entity.user.UserEntity;
import com.free.swd_392.enums.WalletStatus;
import com.free.swd_392.enums.WalletType;
import com.free.swd_392.shared.constant.TableName;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TableName.PAYMENT_WALLET)
public class WalletEntity extends Audit<String> {

    @Id
    @UuidGenerator
    private String id;
    private BigDecimal balance;
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private WalletStatus status;
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private WalletType type;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "merchant_id")
    private String merchantId;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "fk_wallet_user_id"),
            insertable = false,
            updatable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;
    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class)
    @JoinColumn(
            name = "merchant_id",
            foreignKey = @ForeignKey(name = "fk_wallet_merchant_id"),
            insertable = false,
            updatable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MerchantEntity merchant;
}
