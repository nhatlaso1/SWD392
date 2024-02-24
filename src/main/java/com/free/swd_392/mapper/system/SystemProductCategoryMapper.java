package com.free.swd_392.mapper.system;

import com.free.swd_392.core.mapper.BaseMapper;
import com.free.swd_392.dto.product.ProductCategoryInfo;
import com.free.swd_392.entity.product.ProductCategoryEntity;
import com.free.swd_392.shared.utils.PhoneUtils;
import lombok.Setter;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {PhoneUtils.class}
)
@Setter(onMethod_ = {@Autowired})
public abstract class SystemProductCategoryMapper implements BaseMapper<ProductCategoryInfo, ProductCategoryInfo, ProductCategoryEntity> {

    @Override
    @Mapping(target = "children", ignore = true)
    public abstract ProductCategoryEntity createConvertToEntity(ProductCategoryInfo details);

    @Override
    @Mapping(target = "children", ignore = true)
    public abstract void updateConvertToEntity(@MappingTarget ProductCategoryEntity entity, ProductCategoryInfo details);

    @Override
    @Mapping(target = "children", ignore = true)
    public abstract ProductCategoryInfo convertToDetail(ProductCategoryEntity entity);

    @Override
    @Mapping(target = "children", ignore = true)
    public abstract ProductCategoryInfo convertToInfo(ProductCategoryEntity entity);
}
