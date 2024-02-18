package com.free.swd_392.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity(name = TransactionsEntity.TABLE_NAME)
public class TransactionsEntity {
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
    private UsersEntity users;
}
