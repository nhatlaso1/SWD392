package com.free.swd_392.core.converter;

import com.free.swd_392.core.model.IBaseData;

import java.util.ArrayList;
import java.util.List;

public interface ListEntityToListInfoConverter<I, T extends IBaseData<I>, E> extends InfoConverter<I, T, E> {

    default List<T> convertToInfoList(Iterable<E> entities) {
        List<T> response = new ArrayList<>();
        if (entities == null || !entities.iterator().hasNext()) {
            return response;
        }
        for (E entity : entities) {
            response.add(convertToInfo(entity));
        }
        return response;
    }
}
