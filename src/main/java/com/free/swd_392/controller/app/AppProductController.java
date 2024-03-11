package com.free.swd_392.controller.app;

import com.free.swd_392.controller.BaseController;
import com.free.swd_392.core.controller.*;
import com.free.swd_392.core.model.BasePagingResponse;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.dto.product.ProductDetails;
import com.free.swd_392.dto.product.ProductInfo;
import com.free.swd_392.dto.product.request.CreateProductRequest;
import com.free.swd_392.dto.product.request.UpdateProductRequest;
import com.free.swd_392.dto.product.request.filter.AppProductPageFilter;
import com.free.swd_392.dto.product.request.filter.PublicProductPageFilter;
import com.free.swd_392.entity.product.ProductEntity;
import com.free.swd_392.exception.InvalidException;
import com.free.swd_392.mapper.app.AppProductMapper;
import com.free.swd_392.mapper.app.AppSkuMapper;
import com.free.swd_392.repository.product.ProductCategoryRepository;
import com.free.swd_392.repository.product.ProductRepository;
import com.free.swd_392.repository.product.SkuRepository;
import com.free.swd_392.shared.utils.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "App Product Controller")
@RestController
@RequestMapping("/api/v1/app/product")
@RequiredArgsConstructor
public class AppProductController extends BaseController implements
        ICreateModelController<Long, ProductDetails, CreateProductRequest, Long, ProductEntity>,
        IDeleteModelByIdController<Long, Long, ProductEntity>,
        IGetInfoPageWithFilterController<Long, ProductInfo, Long, ProductEntity, AppProductPageFilter>,
        IGetDetailsController<Long, ProductDetails, Long, ProductEntity>,
        IUpdateModelController<Long, ProductDetails, UpdateProductRequest, Long, ProductEntity> {

    private final ProductRepository repository;
    private final ProductCategoryRepository productCategoryRepository;
    private final SkuRepository skuRepository;
    private final AppProductMapper appProductMapper;
    private final AppSkuMapper appSkuMapper;

    @Transactional
    @GetMapping("/public/info/page/filter")
    public ResponseEntity<BasePagingResponse<ProductInfo>> getProductPagePublic(@ParameterObject @Valid PublicProductPageFilter filter,
                                                                                @ParameterObject Pageable pageable) {
        var page = repository.findProductFilter(
                filter.getName(),
                filter.getCategoryIds(),
                filter.getFromPrice(),
                filter.getToPrice(),
                pageable);
        return success(new BasePagingResponse<>(page));
    }

    @Transactional
    @GetMapping("/public/{id}/details")
    public ResponseEntity<BaseResponse<ProductDetails>> getProductDetailsPublic(@PathVariable("id") @NotNull Long productId) {
        var productDetails = aroundGetDetails(productId);
        var sku = skuRepository.getAllByProductIdAndVariantsNotSoldOut(productId);
        if (!CollectionUtils.isEmpty(sku)) {
            var minPrice = sku.get(0).getPrice();
            var maxPrice = sku.get(0).getPrice();
            for (var s : sku) {
                if (minPrice.compareTo(s.getPrice()) > 0) {
                    minPrice = s.getPrice();
                }
                if (maxPrice.compareTo(s.getPrice()) < 0) {
                    maxPrice = s.getPrice();
                }
            }
            productDetails.setFromPrice(minPrice);
            productDetails.setToPrice(maxPrice);
            productDetails.setSkus(appSkuMapper.convertToInfoNoVariantsList(sku));
        }
        return success(productDetails);
    }

    @Override
    public CreateProductRequest preCreate(CreateProductRequest request) {
        productCategoryRepository.checkExits(request.getCategoryId());
        request.setMerchantId(JwtUtils.getMerchantId());
        return request;
    }

    @Override
    public UpdateProductRequest preUpdate(UpdateProductRequest request) {
        if (isProductOwner(request.getId())) {
            return request;
        }
        throw new AccessDeniedException("");
    }

    @Override
    public void preDelete(Long id) {
        if (isProductOwner(id)) {
            IDeleteModelByIdController.super.preDelete(id);
            return;
        }
        throw new AccessDeniedException("");
    }

    @Override
    public void preGetDetails(Long id) {
        if (isProductOwner(id)) {
            IGetDetailsController.super.preGetDetails(id);
            return;
        }
        throw new AccessDeniedException("");
    }

    @Override
    public ProductEntity createConvertToEntity(CreateProductRequest request) {
        return appProductMapper.createConvertToEntity(request);
    }

    @Override
    public void updateConvertToEntity(ProductEntity entity, UpdateProductRequest request) throws InvalidException {
        appProductMapper.updateConvertToEntity(entity, request);
    }

    @Override
    public ProductInfo convertToInfo(ProductEntity entity) {
        return appProductMapper.convertToInfo(entity);
    }

    @Override
    public ProductDetails convertToDetails(ProductEntity entity) {
        return appProductMapper.convertToDetails(entity);
    }

    @Override
    public JpaRepository<ProductEntity, Long> getRepository() {
        return repository;
    }

    @Override
    public String notFound() {
        return "Product not found";
    }

    private boolean isProductOwner(Long id) {
        return repository.existsByIdAndMerchantId(id, JwtUtils.getMerchantId());
    }
}
