package com.free.swd_392.dto;

import com.free.swd_392.core.model.IBaseData;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class AuditInfo<I> implements IBaseData<I> {

    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
