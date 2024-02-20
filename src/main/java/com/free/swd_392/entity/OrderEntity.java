package com.free.swd_392.entity;


import com.free.swd_392.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.fastboot.jpa.entity.Audit;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity(name = OrderEntity.TABLE_NAME)
public class OrderEntity extends Audit<String> {
    public static final String TABLE_NAME = "orders";

    @Id
    @UuidGenerator
    private String id;

    private String orderCode;

    private BigDecimal totalAmount;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
