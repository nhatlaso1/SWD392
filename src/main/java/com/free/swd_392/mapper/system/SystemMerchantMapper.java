package com.free.swd_392.mapper.system;

import com.free.swd_392.core.mapper.DtoMapper;
import com.free.swd_392.dto.merchant.MerchantDetails;
import com.free.swd_392.dto.merchant.MerchantInfo;
import com.free.swd_392.entity.merchant.MerchantEntity;
import com.free.swd_392.mapper.LocationMapper;
import com.free.swd_392.mapper.UserMapper;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {UserMapper.class, LocationMapper.class}
)
public abstract class SystemMerchantMapper implements DtoMapper<MerchantInfo, MerchantDetails, MerchantEntity> {

    @Override
    @Named("convertToInfo")
    @Mapping(target = "provinceName", source = "province", qualifiedByName = "toLocationName")
    @Mapping(target = "districtName", source = "district", qualifiedByName = "toLocationName")
    @Mapping(target = "wardName", source = "ward", qualifiedByName = "toLocationName")
    public abstract MerchantInfo convertToInfo(MerchantEntity entity);

    @Override
    @Named("convertToDetailsMapper")
    @InheritConfiguration(name = "convertToInfo")
    @Mapping(target = "owner", source = "owner", qualifiedByName = "convertToDetailsMapper")
    public abstract MerchantDetails convertToDetails(MerchantEntity entity);
}
