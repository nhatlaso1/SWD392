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
import org.springframework.fastboot.jpa.entity.Audit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity(name = UserEntity.TABLE_NAME)
public class UserEntity extends Audit<String> {
    public static final String TABLE_NAME = "user";

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = TransactionEntity.Fields.user)
    private List<TransactionEntity> transactions;
}
