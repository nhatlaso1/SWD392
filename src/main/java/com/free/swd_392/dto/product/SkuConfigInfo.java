package com.free.swd_392.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.core.view.View;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@JsonView({View.Details.class, View.Include.Create.class, View.Include.Update.class})
public class SkuConfigInfo implements IBaseData<String> {

    @JsonIgnore
    private String id;
    @NotNull
    private Long configId;
    @NotNull
    private Long variantId;
    @JsonView({
            View.Exclude.Create.class,
            View.Exclude.Update.class,
            View.Details.class
    })
    private UUID skuId;
}
