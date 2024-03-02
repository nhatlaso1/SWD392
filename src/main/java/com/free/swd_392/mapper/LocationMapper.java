package com.free.swd_392.mapper;

import com.free.swd_392.core.mapper.DtoMapper;
import com.free.swd_392.dto.location.LocationInfo;
import com.free.swd_392.entity.location.LocationEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public abstract class LocationMapper implements DtoMapper<LocationInfo, LocationInfo, LocationEntity> {
}
