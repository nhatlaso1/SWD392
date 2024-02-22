package com.free.swd_392.entity.user;

import com.free.swd_392.entity.audit.Audit;
import com.free.swd_392.enums.Gender;
import com.free.swd_392.shared.constant.TableName;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = TableName.USER)
public class UserEntity extends Audit<UUID> {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column(length = 50)
    private String name;
    @Column(length = 50)
    private String email;
    @Column(length = 10)
    private String phone;
    @Column(length = 2083)
    private String photo;
    @Column(length = 6)
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private LocalDate birthday;
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private boolean active = true;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = RoleEntity.class, optional = false)
    @JoinColumn(
            name = "role_id",
            foreignKey = @ForeignKey(name = "fk_user_role_id"),
            insertable = false,
            updatable = false
    )
    private RoleEntity role;
}
