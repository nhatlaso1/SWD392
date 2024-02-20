package com.free.swd_392.entity;

import com.free.swd_392.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.fastboot.jpa.entity.Audit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity(name = TransactionEntity.TABLE_NAME)
public class TransactionEntity extends Audit<String> {
    public static final String TABLE_NAME = "transactions";

    @Id
    @UuidGenerator
    private String id;

    private String refTransactionId;

    private BigDecimal amount;

    private String description;

    private LocalDateTime transactionDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private String fromAccountNo;

    private String fromAccountName;

    private String toAccountNo;

    private String toAccountName;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
}
