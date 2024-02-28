package com.free.swd_392.mapper.system;

import com.free.swd_392.core.mapper.CreateModelMapper;
import com.free.swd_392.core.mapper.DtoMapper;
import com.free.swd_392.core.mapper.UpdateModelMapper;
import com.free.swd_392.dto.product.SkuConfigInfo;
import com.free.swd_392.entity.product.SkuConfigEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {SystemProductConfigMapper.class}
)
public abstract class SystemSkuConfigMapper implements
        DtoMapper<SkuConfigInfo, SkuConfigInfo, SkuConfigEntity>,
        CreateModelMapper<SkuConfigInfo, SkuConfigEntity>,
        UpdateModelMapper<SkuConfigInfo, SkuConfigEntity> {

    @Named("createConvertToEntityList")
    @IterableMapping(elementTargetType = SkuConfigEntity.class, qualifiedByName = "createConvertToEntityMapper")
    public abstract List<SkuConfigEntity> createConvertToEntityList(List<SkuConfigInfo> infoList);

    @Named("updateConvertToEntityListMapper")
    public void updateConvertToEntityList(@MappingTarget List<SkuConfigEntity> entities, List<SkuConfigInfo> details) {
        updateConvertToEntityList(
                entities,
                details,
                SkuConfigEntity::getConfigId,
                SkuConfigInfo::getConfigId,
                SkuConfigEntity::new
        );
    }

}
