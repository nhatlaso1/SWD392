package com.free.swd_392.dto.product;

import com.fasterxml.jackson.annotation.JsonView;
import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.core.view.View;
import com.free.swd_392.entity.product.SkuConfigId;
import lombok.Data;

@Data
@JsonView({View.Details.class, View.Include.Create.class, View.Include.Update.class})
public class SkuConfigInfo implements IBaseData<SkuConfigId> {

    private SkuConfigId id;

}
