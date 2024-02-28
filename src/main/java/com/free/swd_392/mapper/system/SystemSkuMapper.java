package com.free.swd_392.mapper.system;

import com.free.swd_392.core.mapper.CreateModelMapper;
import com.free.swd_392.core.mapper.DtoMapper;
import com.free.swd_392.core.mapper.UpdateModelMapper;
import com.free.swd_392.dto.product.SkuInfo;
import com.free.swd_392.dto.product.request.CreateSkuRequest;
import com.free.swd_392.dto.product.request.UpdateSkuRequest;
import com.free.swd_392.entity.product.SkuEntity;
import org.checkerframework.checker.units.qual.N;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {SystemSkuConfigMapper.class}
)
public abstract class SystemSkuMapper implements
        DtoMapper<SkuInfo, SkuInfo, SkuEntity>,
        CreateModelMapper<CreateSkuRequest, SkuEntity>,
        UpdateModelMapper<UpdateSkuRequest, SkuEntity> {

    @Override
    @Named("createConvertToEntityMapper")
    @Mapping(target = "configs", source = "configs", qualifiedByName = "createConvertToEntityList")
    public abstract SkuEntity createConvertToEntity(CreateSkuRequest details);

    @Override
    @Named("updateConvertToEntityListMapper")
    @Mapping(target = "configs", source = "configs", qualifiedByName = "updateConvertToEntityListMapper")
    public abstract void updateConvertToEntity(@MappingTarget SkuEntity entity, UpdateSkuRequest details);

    @Override
    @Named("convertToInfoMapper")
    @Mapping(target = "configs", source = "configs", qualifiedByName = "convertToInfoListMapper")
    public abstract SkuInfo convertToInfo(SkuEntity entity);
}
