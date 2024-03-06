package com.free.swd_392.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Pageable;

public interface IPageFilter extends IFilter {

    @JsonIgnore
    @Hidden
    Pageable getPageable();

    @JsonIgnore
    @Hidden
    void setPageable(Pageable pageable);
}
