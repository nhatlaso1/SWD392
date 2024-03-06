package com.free.swd_392.mapper.app;

import com.free.swd_392.core.mapper.CreateModelMapper;
import com.free.swd_392.core.mapper.DtoMapper;
import com.free.swd_392.core.mapper.UpdateModelMapper;
import com.free.swd_392.dto.merchant.MerchantDetails;
import com.free.swd_392.dto.merchant.MerchantInfo;
import com.free.swd_392.dto.merchant.request.AppCreateMerchantRequest;
import com.free.swd_392.dto.merchant.request.AppUpdateMerchantRequest;
import com.free.swd_392.entity.merchant.MerchantEntity;
import com.free.swd_392.mapper.LocationMapper;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {LocationMapper.class}
)
public abstract class AppMerchantMapper implements
        DtoMapper<MerchantInfo, MerchantDetails, MerchantEntity>,
        CreateModelMapper<AppCreateMerchantRequest, MerchantEntity>,
        UpdateModelMapper<AppUpdateMerchantRequest, MerchantEntity> {

    @Named("updateDraft")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    public abstract void updateDraft(@MappingTarget MerchantEntity entity, AppCreateMerchantRequest request);

    @Named("updateConvertToEntityMapper")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "avatar", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "coverImage", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "addressNote", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "facebook", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "zalo", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "instagram", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "youtube", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    public abstract void updateConvertToEntity(@MappingTarget MerchantEntity entity, AppUpdateMerchantRequest request);

    @Override
    @Named("convertToInfoMapper")
    @Mapping(target = "owner", ignore = true)
    public abstract MerchantDetails convertToDetails(MerchantEntity entity);

    @Override
    @Named("convertToInfoMapper")
    @Mapping(target = "provinceName", source = "province", qualifiedByName = "toLocationName")
    @Mapping(target = "districtName", source = "district", qualifiedByName = "toLocationName")
    @Mapping(target = "wardName", source = "ward", qualifiedByName = "toLocationName")
    public abstract MerchantInfo convertToInfo(MerchantEntity entity);
}
