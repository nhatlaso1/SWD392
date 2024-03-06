package com.free.swd_392.controller.app;

import com.free.swd_392.controller.BaseController;
import com.free.swd_392.core.controller.ICreateModelController;
import com.free.swd_392.core.controller.IDeleteModelByIdController;
import com.free.swd_392.core.controller.IGetInfoListWithFilterController;
import com.free.swd_392.core.controller.IUpdateModelController;
import com.free.swd_392.dto.product.SkuInfo;
import com.free.swd_392.dto.product.request.CreateSkuRequest;
import com.free.swd_392.dto.product.request.UpdateSkuRequest;
import com.free.swd_392.dto.product.request.filter.SystemSkuListFilter;
import com.free.swd_392.entity.product.ProductConfigEntity;
import com.free.swd_392.entity.product.ProductEntity;
import com.free.swd_392.entity.product.ProductVariantEntity;
import com.free.swd_392.entity.product.SkuEntity;
import com.free.swd_392.enums.ProductConfigChoice;
import com.free.swd_392.exception.InvalidException;
import com.free.swd_392.mapper.app.AppSkuMapper;
import com.free.swd_392.repository.product.ProductRepository;
import com.free.swd_392.repository.product.ProductVariantRepository;
import com.free.swd_392.repository.product.SkuRepository;
import com.free.swd_392.shared.utils.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static java.util.stream.Collectors.*;

@Slf4j
@Tag(name = "System Sku Controller")
@RestController
@RequestMapping("/api/v1/app/product-sku")
@RequiredArgsConstructor
public class AppSkuController extends BaseController implements
        ICreateModelController<UUID, SkuInfo, CreateSkuRequest, UUID, SkuEntity>,
        IUpdateModelController<UUID, SkuInfo, UpdateSkuRequest, UUID, SkuEntity>,
        IDeleteModelByIdController<UUID, UUID, SkuEntity>,
        IGetInfoListWithFilterController<UUID, SkuInfo, UUID, SkuEntity, SystemSkuListFilter> {

    private final SkuRepository repository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final AppSkuMapper appSkuMapper;

    @Override
    public CreateSkuRequest preCreate(CreateSkuRequest request) {
        var productEntity = productRepository.findByIdFetchConfigVariant(request.getProductId())
                .orElseThrow(() -> new InvalidException("Product id not found"));
        if (CollectionUtils.isEmpty(productEntity.getProductConfigs())) {
            return request;
        }
        validateProductConfig(productEntity, request.getVariantIds(), null);
        return request;
    }

    @Override
    public void postCreate(SkuEntity entity, CreateSkuRequest request, SkuInfo details) {
        entity.setVariants(productVariantRepository.findAllById(request.getVariantIds()));
        repository.save(entity);
    }

    @Override
    public UpdateSkuRequest preUpdate(UpdateSkuRequest request) {
        var skuEntity = findById(request.getId(), notFound());
        var productEntity = productRepository.findByIdFetchConfigVariant(skuEntity.getProductId())
                .orElseThrow(() -> new InvalidException("Product id not found"));
        if (!Objects.equals(productEntity.getMerchantId(), JwtUtils.getMerchantId())) {
            throw new AccessDeniedException("");
        }
        if (CollectionUtils.isEmpty(productEntity.getProductConfigs())) {
            return request;
        }
        validateProductConfig(productEntity, request.getVariantIds(), request.getId());
        return request;
    }

    @Override
    public void postUpdate(SkuEntity entity, UpdateSkuRequest request, SkuInfo details) {
        entity.setVariants(productVariantRepository.findAllById(request.getVariantIds()));
        repository.save(entity);
    }

    @Override
    public void preDelete(UUID id) {
        if (!repository.existsByIdAndProductMerchantId(id, JwtUtils.getMerchantId())) {
            throw new AccessDeniedException("");
        }
    }

    @Override
    public SkuInfo convertToDetails(SkuEntity entity) {
        return appSkuMapper.convertToDetails(entity);
    }

    @Override
    public SkuInfo convertToInfo(SkuEntity entity) {
        return appSkuMapper.convertToInfo(entity);
    }

    @Override
    public JpaRepository<SkuEntity, UUID> getRepository() {
        return repository;
    }

    @Override
    public SkuEntity createConvertToEntity(CreateSkuRequest details) {
        return appSkuMapper.createConvertToEntity(details);
    }

    @Override
    public void updateConvertToEntity(SkuEntity entity, UpdateSkuRequest details) {
        appSkuMapper.updateConvertToEntity(entity, details);
    }

    @Override
    public String notFound() {
        return "Sku not found";
    }

    private void validateProductConfig(ProductEntity productEntity, Set<Long> variantIds, @Nullable UUID requestId) {
        if (CollectionUtils.isEmpty(variantIds)) {
            throw new InvalidException("Product configs can't empty");
        }
        Map<Long, Set<Long>> configVariantSetMap = toConfigVariantSetMap(productEntity, variantIds);
        Map<Long, Set<Long>> productVariantMap = toProductVariantMap(productEntity);
        for (var config : productEntity.getProductConfigs()) {
            var requestVariants = configVariantSetMap.get(config.getId());
            if (requestVariants == null || CollectionUtils.isEmpty(requestVariants)) {
                throw new InvalidException("Product config " + config.getId() + " must have one or more variants");
            }
            if (ProductConfigChoice.SINGLE_CHOICE.equals(config.getChoiceKind()) && requestVariants.size() > 1) {
                throw new InvalidException("Can't select multiple choice in config " + config.getId());
            }
            var configVariants = productVariantMap.getOrDefault(config.getId(), Collections.emptySet());
            for (var variantId : requestVariants) {
                if (!configVariants.contains(variantId)) {
                    throw new InvalidException("Product variant " + variantId + " not found in config " + config.getId());
                }
            }
        }
        var skus = repository.getAllByProductId(productEntity.getId());
        for (var sku : skus) {
            if (Objects.equals(requestId, sku.getId())) {
                continue;
            }
            if (Set.copyOf(sku.getVariantIds()).containsAll(variantIds)) {
                throw new InvalidException(conflict());
            }
        }
    }

    @NotNull
    private static Map<Long, Set<Long>> toConfigVariantSetMap(ProductEntity productEntity, Set<Long> variantIds) {
        return productEntity.getProductConfigs()
                .stream()
                .flatMap(pc -> pc.getVariants().stream()
                        .filter(pv -> variantIds.contains(pv.getId()))
                        .map(pv -> ImmutablePair.of(pc, pv.getId()))
                )
                .collect(groupingBy(o -> o.getLeft().getId(), HashMap::new, mapping(ImmutablePair::getRight, toSet())));
    }

    @NotNull
    private static Map<Long, Set<Long>> toProductVariantMap(ProductEntity productEntity) {
        return productEntity.getProductConfigs().stream()
                .collect(groupingBy(
                        ProductConfigEntity::getId,
                        HashMap::new,
                        flatMapping(pc -> pc.getVariants().stream().map(ProductVariantEntity::getId), toSet())
                ));
    }

    @Override
    public String conflict() {
        return "Duplicate sku";
    }
}
