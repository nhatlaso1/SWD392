package com.free.swd_392.controller.system;

import com.free.swd_392.controller.BaseController;
import com.free.swd_392.core.controller.*;
import com.free.swd_392.dto.product.ProductDetails;
import com.free.swd_392.dto.product.ProductInfo;
import com.free.swd_392.dto.product.request.CreateProductRequest;
import com.free.swd_392.dto.product.request.filter.SystemProductPageFilter;
import com.free.swd_392.dto.product.request.UpdateProductRequest;
import com.free.swd_392.entity.product.ProductEntity;
import com.free.swd_392.exception.InvalidException;
import com.free.swd_392.mapper.system.SystemProductMapper;
import com.free.swd_392.repository.product.ProductCategoryRepository;
import com.free.swd_392.repository.product.ProductRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "System Product Controller")
@RestController
@RequestMapping("/api/v1/system/product")
@RequiredArgsConstructor
public class SystemProductController extends BaseController implements
        ICreateModelController<Long, ProductDetails, CreateProductRequest, Long, ProductEntity>,
        IDeleteModelByIdController<Long, Long, ProductEntity>,
        IGetInfoPageWithFilterController<Long, ProductInfo, Long, ProductEntity, SystemProductPageFilter>,
        IGetDetailsController<Long, ProductDetails, Long, ProductEntity>,
        IUpdateModelController<Long, ProductDetails, UpdateProductRequest, Long, ProductEntity> {

    private final ProductRepository repository;
    private final ProductCategoryRepository productCategoryRepository;
    private final SystemProductMapper systemProductMapper;

    @Override
    public CreateProductRequest preCreate(CreateProductRequest request) {
        productCategoryRepository.checkExits(request.getCategoryId());
        return request;
    }

    @Override
    public ProductEntity createConvertToEntity(CreateProductRequest request) {
        return systemProductMapper.createConvertToEntity(request);
    }

    @Override
    public void updateConvertToEntity(ProductEntity entity, UpdateProductRequest request) throws InvalidException {
        systemProductMapper.updateConvertToEntity(entity, request);
    }

    @Override
    public ProductInfo convertToInfo(ProductEntity entity) {
        return systemProductMapper.convertToInfo(entity);
    }

    @Override
    public ProductDetails convertToDetails(ProductEntity entity) {
        return systemProductMapper.convertToDetails(entity);
    }

    @Override
    public JpaRepository<ProductEntity, Long> getRepository() {
        return repository;
    }

    @Override
    public String notFound() {
        return "Product not found";
    }
}
