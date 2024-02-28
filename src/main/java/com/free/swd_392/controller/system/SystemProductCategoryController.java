package com.free.swd_392.controller.system;

import com.free.swd_392.controller.BaseController;
import com.free.swd_392.core.controller.*;
import com.free.swd_392.core.model.SuccessResponse;
import com.free.swd_392.dto.product.ProductCategoryInfo;
import com.free.swd_392.dto.product.request.ChangeOrderProductCategoryRequest;
import com.free.swd_392.dto.product.request.filter.ProductCategoryFilter;
import com.free.swd_392.entity.product.ProductCategoryEntity;
import com.free.swd_392.exception.InvalidException;
import com.free.swd_392.mapper.system.SystemProductCategoryMapper;
import com.free.swd_392.repository.product.ProductCategoryRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "System Product Category Controller")
@Transactional
@RestController
@RequestMapping("/api/v1/system/product-category")
@RequiredArgsConstructor
public class SystemProductCategoryController extends BaseController implements
        ICreateModelController<Long, ProductCategoryInfo, ProductCategoryInfo, Long, ProductCategoryEntity>,
        IUpdateModelController<Long, ProductCategoryInfo, ProductCategoryInfo, Long, ProductCategoryEntity>,
        IDeleteModelByIdController<Long, Long, ProductCategoryEntity>,
        IGetInfoListWithFilterController<Long, ProductCategoryInfo, Long, ProductCategoryEntity, ProductCategoryFilter>,
        IGetDetailsController<Long, ProductCategoryInfo, Long, ProductCategoryEntity> {

    private final SystemProductCategoryMapper systemProductCategoryMapper;
    @Getter
    private final ProductCategoryRepository repository;

    @PutMapping(value = "/changeOrder")
    public SuccessResponse changeOrder(@Valid @RequestBody ChangeOrderProductCategoryRequest request) {
        var productCategoryEntity = findById(request.getId(), notFound());
        int newOrder = request.getNewOrder() + 1;
        if (productCategoryEntity.getOrdering() == newOrder) {
            return SuccessResponse.SUCCESS;
        }
        int coefficient = 1;
        int start = newOrder;
        int end = productCategoryEntity.getOrdering();
        if (productCategoryEntity.getOrdering() < newOrder) {
            coefficient = -1;
            start = productCategoryEntity.getOrdering();
            end = newOrder;
        }
        repository.updateOrdering(productCategoryEntity.getParentId(), start, end, coefficient);
        productCategoryEntity.setOrdering(newOrder);
        repository.save(productCategoryEntity);
        return SuccessResponse.SUCCESS;
    }

    @Override
    public ProductCategoryInfo preCreate(ProductCategoryInfo details) {
        if (details.getParentId() != null && !repository.existsById(details.getParentId())) {
            throw new InvalidException("Parent category not found");
        }
        int ordering = repository.countByParentId(details.getParentId()) + 1;
        details.setOrdering(ordering);
        return details;
    }

    @Override
    public void preDelete(Long id) {
        var productCategoryEntity = findById(id, notFound());
        repository.updateOrdering(
                productCategoryEntity.getParentId(),
                productCategoryEntity.getOrdering(),
                Integer.MAX_VALUE,
                -1
        );
    }

    @Override
    public ProductCategoryEntity createConvertToEntity(ProductCategoryInfo details) {
        return systemProductCategoryMapper.createConvertToEntity(details);
    }

    @Override
    public void updateConvertToEntity(ProductCategoryEntity entity, ProductCategoryInfo details) throws InvalidException {
        systemProductCategoryMapper.updateConvertToEntity(entity, details);
    }

    @Override
    public ProductCategoryInfo convertToInfo(ProductCategoryEntity entity) {
        return systemProductCategoryMapper.convertToInfo(entity);
    }

    @Override
    public ProductCategoryInfo convertToDetails(ProductCategoryEntity entity) {
        return systemProductCategoryMapper.convertToDetails(entity);
    }

    @Override
    public String notFound() {
        return "Product category not found";
    }
}
