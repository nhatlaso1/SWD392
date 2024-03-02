package com.free.swd_392.entity.product;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class SkuConfigId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID skuId;
    private Long variantId;
}
