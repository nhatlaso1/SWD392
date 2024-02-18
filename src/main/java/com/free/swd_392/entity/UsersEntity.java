package com.free.swd_392.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity(name = UsersEntity.TABLE_NAME)
public class UsersEntity {
    public static final String TABLE_NAME = "users";

    @Id
    @UuidGenerator
    private String id;

    private String fullName;

    private String photo;

    private String email;

    private String sex;

    private LocalDate birthday;

    private String phoneNumber;

    private BigDecimal balance;

    private boolean status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = TransactionsEntity.Fields.users)
    private List<TransactionsEntity> transactions;
}
