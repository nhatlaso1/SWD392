package com.free.swd_392.entity;

import com.free.swd_392.enums.CategoryStatus;
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
@Entity(name = CategoryEntity.TABLE_NAME)
public class CategoryEntity extends Audit<String> {
    public static final String TABLE_NAME = "categories";

    @Id
    @UuidGenerator
    private String id;

    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    private CategoryStatus status;
}
