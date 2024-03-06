package com.free.swd_392.entity.merchant;

import com.free.swd_392.entity.audit.Audit;
import com.free.swd_392.entity.location.LocationEntity;
import com.free.swd_392.entity.user.UserEntity;
import com.free.swd_392.enums.MerchantStatus;
import com.free.swd_392.shared.constant.TableName;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = TableName.MERCHANT)
public class MerchantEntity extends Audit<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Merchant info
    @Column(columnDefinition = "VARCHAR(100) NOT NULL, FULLTEXT KEY nameFullText(name)")
    private String name;
    @Column(length = 10)
    private String phone;
    @Column(length = 2083)
    private String avatar;
    @Column(length = 2083)
    private String coverImage;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private MerchantStatus status;

    // Address
    @Column(name = "province_id", nullable = false)
    private Long provinceId;
    @Column(name = "district_id", nullable = false)
    private Long districtId;
    @Column(name = "ward_id", nullable = false)
    private Long wardId;
    private String addressDetails;
    @Column(length = 1000)
    private String addressNote;

    // Representative info
    @Column(length = 50)
    private String representativeName;
    @Column(length = 50)
    private String representativeEmail;
    @Column(length = 50)
    private String representativePhone;
    @Column(length = 2083)
    private String idFront;
    @Column(length = 2083)
    private String idBack;
    @Column(name = "owner_id", nullable = false)
    private String ownerId;
    @Column(length = 1000)
    private String approvalReason;

    // Social
    private String facebook;
    private String zalo;
    private String instagram;
    private String youtube;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, targetEntity = UserEntity.class, optional = false)
    @JoinColumn(
            name = "owner_id",
            foreignKey = @ForeignKey(name = "fk_merchant_owner_user_id"),
            insertable = false,
            updatable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity owner;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = LocationEntity.class)
    @JoinColumn(
            name = "province_id",
            foreignKey = @ForeignKey(name = "fk_merchant_province_location_id"),
            insertable = false,
            updatable = false
    )
    private LocationEntity province;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = LocationEntity.class)
    @JoinColumn(
            name = "district_id",
            foreignKey = @ForeignKey(name = "fk_merchant_district_location_id"),
            insertable = false,
            updatable = false
    )
    private LocationEntity district;
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = LocationEntity.class)
    @JoinColumn(
            name = "ward_id",
            foreignKey = @ForeignKey(name = "fk_merchant_ward_location_id"),
            insertable = false,
            updatable = false
    )
    private LocationEntity ward;
}
