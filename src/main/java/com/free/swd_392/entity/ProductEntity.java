package com.free.swd_392.entity;

import com.free.swd_392.enums.ProductStatus;
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
@Entity(name = ProductEntity.TABLE_NAME)
public class ProductEntity extends Audit<String> {
    public static final String TABLE_NAME = "products";

    @Id
    @UuidGenerator
    private String id;

    private String name;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    private String color;

    private String size;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;
}
