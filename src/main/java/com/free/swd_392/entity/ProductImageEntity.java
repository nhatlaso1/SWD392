package com.free.swd_392.entity;


import com.free.swd_392.enums.ImageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.fastboot.jpa.entity.Audit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity(name = ProductImageEntity.TABLE_NAME)
public class ProductImageEntity extends Audit<String> {
    public static final String TABLE_NAME = "product_images";

    @Id
    @UuidGenerator
    private String id;

    private String imageUrl;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    private ImageStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;
}
