package com.free.swd_392.core.converter;

import com.free.swd_392.core.model.IBaseData;

import java.util.ArrayList;
import java.util.List;

public interface ListEntityToListDetailsConverter<I, D extends IBaseData<I>, E> extends DetailsConverter<I, D, E> {

    default List<D> convertToDetailsList(Iterable<E> entities) {
        List<D> response = new ArrayList<>();
        if (entities == null || !entities.iterator().hasNext()) {
            return response;
        }
        for (E entity : entities) {
            response.add(convertToDetails(entity));
        }
        return response;
    }
}
