package com.free.swd_392.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Pageable;

public interface IPageFilter extends IFilter {

    @JsonIgnore
    Pageable getPageable();

    @JsonIgnore
    void setPageable(Pageable pageable);
}
