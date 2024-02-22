package com.free.swd_392.entity.user;

import com.free.swd_392.entity.audit.Audit;
import com.free.swd_392.enums.GroupKind;
import com.free.swd_392.shared.constant.TableName;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(
        name = TableName.USER_ROLE,
        indexes = {
                @Index(name = "idx_kind", columnList = "kind")
        }
)
public class RoleEntity extends Audit<UUID> {
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private GroupKind kind;
}
