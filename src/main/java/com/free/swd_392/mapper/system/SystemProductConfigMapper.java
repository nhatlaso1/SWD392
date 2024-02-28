package com.free.swd_392.mapper.system;

import com.free.swd_392.core.mapper.CreateModelMapper;
import com.free.swd_392.core.mapper.DtoMapper;
import com.free.swd_392.core.mapper.UpdateModelMapper;
import com.free.swd_392.dto.product.ProductConfigInfo;
import com.free.swd_392.entity.product.ProductConfigEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {SystemProductVariantMapper.class}
)
public abstract class SystemProductConfigMapper implements
        DtoMapper<ProductConfigInfo, ProductConfigInfo, ProductConfigEntity>,
        CreateModelMapper<ProductConfigInfo, ProductConfigEntity>,
        UpdateModelMapper<ProductConfigInfo, ProductConfigEntity> {

    @Override
    @Named("createConvertToEntity")
    @Mapping(target = "variants", source = "variants", qualifiedByName = "createConvertToEntityListProductVariant")
    public abstract ProductConfigEntity createConvertToEntity(ProductConfigInfo details);

    @Named("createConvertToEntityList")
    @IterableMapping(elementTargetType = ProductConfigEntity.class, qualifiedByName = "createConvertToEntity")
    public abstract List<ProductConfigEntity> createConvertToEntityList(List<ProductConfigInfo> detailsList);

    @Override
    @Mapping(target = "variants", source = "variants", qualifiedByName = "updateConvertToEntityListProductVariant")
    public abstract void updateConvertToEntity(@MappingTarget ProductConfigEntity entity, ProductConfigInfo details);

    @Named("updateConvertToEntityListMapper")
    public void updateConvertToEntityList(@MappingTarget List<ProductConfigEntity> entity, List<ProductConfigInfo> details) {
        UpdateModelMapper.super.updateConvertToEntityList(
                entity,
                details,
                ProductConfigEntity::getId,
                ProductConfigInfo::getId,
                ProductConfigEntity::new
        );
    }

    @Override
    @Named("convertToInfoMapper")
    @Mapping(target = "variants", source = "variants", qualifiedByName = "convertToInfoListMapper")
    public abstract ProductConfigInfo convertToInfo(ProductConfigEntity entity);
}
