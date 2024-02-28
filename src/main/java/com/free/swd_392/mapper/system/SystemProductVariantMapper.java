package com.free.swd_392.mapper.system;

import com.free.swd_392.core.mapper.CreateModelMapper;
import com.free.swd_392.core.mapper.DtoMapper;
import com.free.swd_392.core.mapper.UpdateModelMapper;
import com.free.swd_392.dto.product.ProductVariantInfo;
import com.free.swd_392.entity.product.ProductVariantEntity;
import lombok.Setter;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
@Setter(onMethod_ = {@Autowired})
public abstract class SystemProductVariantMapper implements
        DtoMapper<ProductVariantInfo, ProductVariantInfo, ProductVariantEntity>,
        CreateModelMapper<ProductVariantInfo, ProductVariantEntity>,
        UpdateModelMapper<ProductVariantInfo, ProductVariantEntity> {

    @Named("createConvertToEntityListProductVariant")
    @IterableMapping(elementTargetType = ProductVariantEntity.class, qualifiedByName = "createConvertToEntityMapper")
    public abstract List<ProductVariantEntity> createConvertToEntityList(List<ProductVariantInfo> requests);

    @Named("updateConvertToEntityListProductVariant")
    public void updateConvertToEntityList(@MappingTarget List<ProductVariantEntity> entities, List<ProductVariantInfo> details) {
        updateConvertToEntityList(
                entities,
                details,
                ProductVariantEntity::getId,
                ProductVariantInfo::getId,
                ProductVariantEntity::new
        );
    }
}
