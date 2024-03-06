package com.free.swd_392.mapper.app;

import com.free.swd_392.core.mapper.CreateModelMapper;
import com.free.swd_392.core.mapper.DtoMapper;
import com.free.swd_392.core.mapper.UpdateModelMapper;
import com.free.swd_392.dto.user.UserAddressInfo;
import com.free.swd_392.dto.user.request.CreateUserAddressRequest;
import com.free.swd_392.dto.user.request.UpdateUserAddressRequest;
import com.free.swd_392.entity.user.UserAddressEntity;
import com.free.swd_392.mapper.LocationMapper;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {LocationMapper.class}
)
public abstract class AppUserAddressMapper implements
        DtoMapper<UserAddressInfo, UserAddressInfo, UserAddressEntity>,
        CreateModelMapper<CreateUserAddressRequest, UserAddressEntity>,
        UpdateModelMapper<UpdateUserAddressRequest, UserAddressEntity> {

    @Override
    @Named("convertToInfoMapper")
    @Mapping(target = "provinceName", source = "province", qualifiedByName = "toLocationName")
    @Mapping(target = "districtName", source = "district", qualifiedByName = "toLocationName")
    @Mapping(target = "wardName", source = "ward", qualifiedByName = "toLocationName")
    public abstract UserAddressInfo convertToInfo(UserAddressEntity entity);
}
