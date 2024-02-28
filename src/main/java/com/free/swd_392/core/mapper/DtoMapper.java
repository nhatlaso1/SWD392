package com.free.swd_392.core.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface DtoMapper<T, D extends T, E> {

    @Named("convertToInfoMapper")
    T convertToInfo(E entity);

    @Named("convertToInfoListMapper")
    default List<T> convertToInfoList(List<E> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>(entities.size());
        for (var entity : entities) {
            result.add(convertToInfo(entity));
        }
        return result;
    }

    @InheritConfiguration(name = "convertToInfo")
    @Named("convertToDetailsMapper")
    D convertToDetails(E entity);

    @Named("convertToDetailsListMapper")
    default List<D> convertToDetailsList(List<E> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        List<D> result = new ArrayList<>(entities.size());
        for (var entity : entities) {
            result.add(convertToDetails(entity));
        }
        return result;
    }
}
