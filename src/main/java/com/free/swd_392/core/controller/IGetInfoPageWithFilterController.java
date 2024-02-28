package com.free.swd_392.core.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.free.swd_392.core.converter.IdConverter;
import com.free.swd_392.core.converter.ListEntityToListInfoConverter;
import com.free.swd_392.core.factory.Factory;
import com.free.swd_392.core.factory.FactoryExceptionCode;
import com.free.swd_392.core.model.BasePagingResponse;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.core.model.IPageFilter;
import com.free.swd_392.core.view.View;
import com.free.swd_392.shared.utils.TypeUtils;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
public interface IGetInfoPageWithFilterController<I, T extends IBaseData<I>, A, E, F extends IPageFilter> extends
        Factory<A, E>,
        FactoryExceptionCode,
        IdConverter<I, A>,
        ListEntityToListInfoConverter<I, T, E> {

    @GetMapping("info/page/filter")
    @PageableAsQueryParam
    @JsonView(View.Info.class)
    @Transactional
    default ResponseEntity<BaseResponse<BasePagingResponse<T>>> getInfoPageWithFilter(
            @ParameterObject @Valid F filter,
            @ParameterObject Pageable pageable
    ) {
        filter.setPageable(pageable);
        F preFilter = preGetPageInfoWithFilter(filter);
        return success(aroundGetPageInfoWithFilter(preFilter));
    }

    default F preGetPageInfoWithFilter(F filter) {
        return filter;
    }

    default BasePagingResponse<T> aroundGetPageInfoWithFilter(F filter) {

        var repository = getRepository();
        Page<E> pageEntity;
        try {
            Specification<E> specification = TypeUtils.unwrap(filter);
            JpaSpecificationExecutor<E> executor = TypeUtils.unwrap(repository);
            pageEntity = executor.findAll(specification, filter.getPageable());
            return new BasePagingResponse<>(pageEntity, this::convertToInfoList);
        } catch (ClassCastException e) {
            // empty
        }
        pageEntity = getRepository().findAll(filter.getPageable());
        return new BasePagingResponse<>(pageEntity, this::convertToInfoList);
    }
}
