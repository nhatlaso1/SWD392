package com.free.swd_392.entity;


import com.free.swd_392.enums.WalletStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.fastboot.jpa.entity.Audit;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = WalletEntity.TABLE_NAME)
public class WalletEntity extends Audit<String> {
    public static final String TABLE_NAME = "wallet";

    @Id
    @UuidGenerator
    private String id;

    private BigDecimal balance;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private String accountNo;

    private String accountName;

    @Enumerated(EnumType.STRING)
    private WalletStatus status;
}
