package com.free.swd_392.mapper.system;

import com.free.swd_392.core.mapper.CreateModelMapper;
import com.free.swd_392.core.mapper.DtoMapper;
import com.free.swd_392.core.mapper.UpdateModelMapper;
import com.free.swd_392.dto.product.ProductDetails;
import com.free.swd_392.dto.product.ProductInfo;
import com.free.swd_392.dto.product.request.CreateProductRequest;
import com.free.swd_392.dto.product.request.UpdateProductRequest;
import com.free.swd_392.entity.product.ProductEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {SystemProductConfigMapper.class}
)
public abstract class SystemProductMapper implements
        DtoMapper<ProductInfo, ProductDetails, ProductEntity>,
        CreateModelMapper<CreateProductRequest, ProductEntity>,
        UpdateModelMapper<UpdateProductRequest, ProductEntity> {

    @Override
    @Named("createConvertToEntity")
    @Mapping(target = "productConfigs", source = "productConfigs", qualifiedByName = "createConvertToEntityList")
    public abstract ProductEntity createConvertToEntity(CreateProductRequest details);

    @Override
    @Named("updateConvertToEntity")
    @Mapping(target = "productConfigs", source = "productConfigs", qualifiedByName = "updateConvertToEntityListMapper")
    public abstract void updateConvertToEntity(@MappingTarget ProductEntity entity, UpdateProductRequest details);

    @Override
    @Named("convertToInfoMapper")
    @Mapping(target = "categoryName", source = "category.name")
    public abstract ProductInfo convertToInfo(ProductEntity entity);

    @Override
    @InheritConfiguration(name = "convertToInfo")
    @Named("convertToDetailMapper")
    @Mapping(target = "productConfigs", source = "productConfigs", qualifiedByName = "convertToInfoListMapper")
    public abstract ProductDetails convertToDetails(ProductEntity entity);
}
