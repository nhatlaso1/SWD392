package com.free.swd_392.core.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

public interface BaseMapper<T, D extends T, E> {
    @Named("createConvertToEntityMapper")
    E createConvertToEntity(D details);

    @Named("updateConvertToEntityMapper")
    void updateConvertToEntity(@MappingTarget E entity, D details);

    @InheritConfiguration(name = "convertToInfo")
    @Named("convertToDetailMapper")
    D convertToDetail(E entity);

    @Named("convertToInfoMapper")
    T convertToInfo(E entity);
}
