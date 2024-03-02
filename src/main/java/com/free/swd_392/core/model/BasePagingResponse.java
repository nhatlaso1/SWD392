package com.free.swd_392.core.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Getter
@JsonPropertyOrder({"success", "pageNumber", "pageSize", "totalElements", "totalPages", "data"})
@EqualsAndHashCode(callSuper = true)
public class BasePagingResponse<T> extends BaseResponse<List<T>> {

    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final long totalPages;

    public BasePagingResponse(List<T> data, int pageNumber, int pageSize, long totalElements, long totalPages) {
        setData(data);
        setSuccess(true);
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public BasePagingResponse(Page<T> page) {
        this(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    public <E> BasePagingResponse(Page<E> page, Function<List<E>, List<T>> mapper) {
        this(mapper.apply(page.getContent()), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
