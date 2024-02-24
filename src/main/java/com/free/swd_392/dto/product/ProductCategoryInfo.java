package com.free.swd_392.dto.product;

import com.fasterxml.jackson.annotation.JsonView;
import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.core.view.View;
import com.free.swd_392.enums.ProductCategoryStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@JsonView(View.Details.class)
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryInfo implements IBaseData<Long> {

    @JsonView({
            View.Exclude.Create.class,
            View.Include.Update.class
    })
    private Long id;
    @JsonView({
            View.Include.Create.class,
            View.Include.Update.class
    })
    private String name;
    @JsonView({
            View.Exclude.Create.class,
            View.Exclude.Update.class
    })
    private Integer ordering;
    @JsonView({
            View.Include.Create.class,
            View.Include.Update.class
    })
    private String icon;
    @JsonView({
            View.Include.Create.class,
            View.Include.Update.class
    })
    private String description;
    @JsonView({
            View.Include.Create.class,
            View.Include.Update.class
    })
    private ProductCategoryStatus status;
    @JsonView({
            View.Include.Create.class,
            View.Exclude.Update.class
    })
    private Long parentId;
    @JsonView({
            View.Exclude.Create.class,
            View.Exclude.Update.class
    })
    private List<ProductCategoryInfo> children;

    public ProductCategoryInfo(Long id, String description, String icon, String name, Integer ordering, Long parentId, String status) {
        this.id = id;
        this.description = description;
        this.icon = icon;
        this.name = name;
        this.ordering = ordering;
        this.parentId = parentId;
        this.status = ProductCategoryStatus.valueOf(status);
    }

    public ProductCategoryInfo addChild(ProductCategoryInfo child) {
        if (children == null) {
            children = new LinkedList<>();
        }
        children.add(child);
        return this;
    }
}
