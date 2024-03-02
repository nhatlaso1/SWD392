package com.free.swd_392.entity.user;

import com.free.swd_392.entity.location.LocationEntity;
import com.free.swd_392.shared.constant.TableName;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
@FieldNameConstants
@Table(name = TableName.USER_ADDRESS)
public class UserAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 200)
    private String addressDetails;
    @Column(length = 50)
    private String receiverFullName;
    @Column(length = 10)
    private String phone;
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDefault = false;
    @Column(length = 200)
    private String note;
    @Column(name = "user_id", insertable = false, updatable = false)
    private String userId;
    @Column(name = "province_id", nullable = false)
    private Long provinceId;
    @Column(name = "district_id", nullable = false)
    private Long districtId;
    @Column(name = "ward_id", nullable = false)
    private Long wardId;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class)
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "fk_user_address_user_id"),
            insertable = false,
            updatable = false
    )
    private UserEntity user;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = LocationEntity.class)
    @JoinColumn(
            name = "province_id",
            foreignKey = @ForeignKey(name = "fk_user_address_province_location_id"),
            insertable = false,
            updatable = false
    )
    private LocationEntity province;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = LocationEntity.class)
    @JoinColumn(
            name = "district_id",
            foreignKey = @ForeignKey(name = "fk_user_address_district_location_id"),
            insertable = false,
            updatable = false
    )
    private LocationEntity district;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = LocationEntity.class)
    @JoinColumn(
            name = "ward_id",
            foreignKey = @ForeignKey(name = "fk_user_address_ward_location_id"),
            insertable = false,
            updatable = false
    )
    private LocationEntity ward;
}
