package com.free.swd_392.mapper;

import com.free.swd_392.core.mapper.DtoMapper;
import com.free.swd_392.dto.user.RoleInfo;
import com.free.swd_392.entity.user.RoleEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public abstract class RoleMapper implements DtoMapper<RoleInfo, RoleInfo, RoleEntity> {
}
