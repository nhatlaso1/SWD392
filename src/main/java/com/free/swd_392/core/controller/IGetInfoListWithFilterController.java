package com.free.swd_392.core.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.free.swd_392.core.converter.IdConverter;
import com.free.swd_392.core.converter.ListEntityToListInfoConverter;
import com.free.swd_392.core.factory.Factory;
import com.free.swd_392.core.factory.FactoryExceptionCode;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.core.model.IBaseData;
import com.free.swd_392.core.model.IFilter;
import com.free.swd_392.core.view.View;
import com.free.swd_392.shared.utils.TypeUtils;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/")
public interface IGetInfoListWithFilterController<I, T extends IBaseData<I>, A, E, F extends IFilter> extends
        Factory<A, E>,
        FactoryExceptionCode,
        IdConverter<I, A>,
        ListEntityToListInfoConverter<I, T, E> {

    @GetMapping("list")
    @JsonView(View.Info.class)
    default ResponseEntity<BaseResponse<List<T>>> getInfoListWithFilter(@ParameterObject @Valid F filter) {
        F preFilter = preGetInfoListWithFilter(filter);
        return success(aroundGetInfoListWithFilter(preFilter));
    }

    default F preGetInfoListWithFilter(F filter) {
        return filter;
    }

    default List<T> aroundGetInfoListWithFilter(F filter) {
        var repository = getRepository();
        Specification<E> specification = TypeUtils.unwrap(filter);
        JpaSpecificationExecutor<E> executor = TypeUtils.unwrap(repository);
        if (specification != null && executor != null) {
            return convertToInfoList(executor.findAll(specification));
        }
        return convertToInfoList(getRepository().findAll());
    }
}
