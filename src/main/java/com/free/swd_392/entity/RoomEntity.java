package com.free.swd_392.entity;

import com.free.swd_392.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity(name = UserEntity.TABLE_NAME)
public class RoomEntity {
    public static final String TABLE_NAME = "auction";

    @Id
    @UuidGenerator
    private String id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_host", nullable = false)
    private UserEntity userHost;

    private LocalDateTime openDate;

    private LocalDateTime closeDate;

    private String description;

    private String thumbUrl;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;
}
