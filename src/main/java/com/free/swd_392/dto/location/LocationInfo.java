package com.free.swd_392.dto.location;

import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.enums.LocationKind;
import lombok.Data;

@Data
public class LocationInfo implements IBaseData<Long> {

    private Long id;
    private String name;
    private LocationKind kind;
    private Long parentId;
}
