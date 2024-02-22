package com.free.swd_392.entity.payment;

import com.free.swd_392.entity.user.UserEntity;
import com.free.swd_392.entity.audit.Audit;
import com.free.swd_392.enums.TransactionStatus;
import com.free.swd_392.shared.constant.TableName;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = TableName.PAYMENT_TRANSACTION)
public class TransactionEntity extends Audit<UUID> {

    @Id
    @UuidGenerator
    private UUID id;
    private String refTransactionId;
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionDate;
    private String fromAccountNo;
    private String fromAccountName;
    private String toAccountNo;
    private String toAccountName;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class, optional = false)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "fk_transaction_user_id"),
            insertable = false,
            updatable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;
}
