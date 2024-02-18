package com.free.swd_392.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Entity(name = UsersEntity.TABLE_NAME)
public class AuctionEntity {
    public static final String TABLE_NAME = "auction";

    @Id
    @UuidGenerator
    private String id;

    private BigDecimal minCost;

    private BigDecimal maxCost;

    private BigDecimal balance;

    private LocalDateTime auctionDate;

}
