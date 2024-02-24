package com.free.swd_392.core.converter;

import com.free.swd_392.exception.InvalidException;
import com.free.swd_392.shared.utils.TypeUtils;

public interface IdConverter<I, A> {

    default A toIdEntity(I id) {
        try {
            return TypeUtils.unwrap(id);
        } catch (ClassCastException e) {
            throw new InvalidException("Can not cast id");
        }
    }

    default I toIdDto(A id) {
        try {
            return TypeUtils.unwrap(id);
        } catch (ClassCastException e) {
            throw new InvalidException("Can not cast id");
        }
    }
}
