package com.free.swd_392.controller.system;

import com.free.swd_392.controller.BaseController;
import com.free.swd_392.core.controller.ICreateModelController;
import com.free.swd_392.core.controller.IDeleteModelByIdController;
import com.free.swd_392.core.controller.IGetInfoListWithFilterController;
import com.free.swd_392.core.controller.IUpdateModelController;
import com.free.swd_392.dto.product.SkuConfigInfo;
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
import com.free.swd_392.mapper.system.SystemSkuMapper;
import com.free.swd_392.repository.product.ProductRepository;
import com.free.swd_392.repository.product.SkuRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static java.util.stream.Collectors.*;

@Slf4j
@Tag(name = "System Sku Controller")
@RestController
@RequestMapping("/api/v1/system/product-sku")
@RequiredArgsConstructor
public class SystemSkuController extends BaseController implements
        ICreateModelController<UUID, SkuInfo, CreateSkuRequest, UUID, SkuEntity>,
        IUpdateModelController<UUID, SkuInfo, UpdateSkuRequest, UUID, SkuEntity>,
        IDeleteModelByIdController<UUID, UUID, SkuEntity>,
        IGetInfoListWithFilterController<UUID, SkuInfo, UUID, SkuEntity, SystemSkuListFilter> {

    private final SkuRepository repository;
    private final ProductRepository productRepository;
    private final SystemSkuMapper systemProductMapper;

    @Override
    public CreateSkuRequest preCreate(CreateSkuRequest request) {
        var productEntity = productRepository.findByIdFetchConfigVariant(request.getProductId())
                .orElseThrow(() -> new InvalidException("Product id not found"));
        if (CollectionUtils.isEmpty(productEntity.getProductConfigs())) {
            return request;
        }
        validateProductConfig(productEntity, request.getConfigs());
        return request;
    }

    @Override
    public UpdateSkuRequest preUpdate(UpdateSkuRequest request) {
        var skuEntity = findById(request.getId(), notFound());
        var productEntity = productRepository.findByIdFetchConfigVariant(skuEntity.getProductId())
                .orElseThrow(() -> new InvalidException("Product id not found"));
        if (CollectionUtils.isEmpty(productEntity.getProductConfigs())) {
            return request;
        }
        validateProductConfig(productEntity, request.getConfigs());
        return request;
    }

    @Override
    public SkuInfo convertToDetails(SkuEntity entity) {
        return systemProductMapper.convertToDetails(entity);
    }

    @Override
    public SkuInfo convertToInfo(SkuEntity entity) {
        return systemProductMapper.convertToInfo(entity);
    }

    @Override
    public JpaRepository<SkuEntity, UUID> getRepository() {
        return repository;
    }

    @Override
    public SkuEntity createConvertToEntity(CreateSkuRequest details) {
        return systemProductMapper.createConvertToEntity(details);
    }

    @Override
    public void updateConvertToEntity(SkuEntity entity, UpdateSkuRequest details) {
        systemProductMapper.updateConvertToEntity(entity, details);
    }

    @Override
    public String notFound() {
        return "Sku not found";
    }

    private void validateProductConfig(ProductEntity productEntity, List<SkuConfigInfo> requestConfigs) {
        if (CollectionUtils.isEmpty(requestConfigs)) {
            throw new InvalidException("Product configs can't empty");
        }
        Map<Long, Set<Long>> configVariantSetMap = requestConfigs.stream()
                .collect(groupingBy(SkuConfigInfo::getConfigId, HashMap::new, mapping(SkuConfigInfo::getVariantId, toSet())));
        Map<Long, ProductConfigChoice> productConfigChoiceMap = productEntity.getProductConfigs().stream()
                .collect(toMap(
                        ProductConfigEntity::getId,
                        ProductConfigEntity::getChoiceKind
                ));
        Map<Long, Set<Long>> productVariantMap = productEntity.getProductConfigs().stream()
                .collect(groupingBy(
                        ProductConfigEntity::getId,
                        HashMap::new,
                        flatMapping(pc -> pc.getVariants().stream().map(ProductVariantEntity::getId), toSet())
                ));
        for (var entry : configVariantSetMap.entrySet()) {
            var configId = entry.getKey();
            var choiceKind = productConfigChoiceMap.get(configId);
            if (choiceKind == null) {
                throw new InvalidException("Product config " + configId + " not found");
            }
            var variantIds = entry.getValue();
            if (ProductConfigChoice.SINGLE_CHOICE.equals(choiceKind)
                    && variantIds.size() > 1) {
                throw new InvalidException("Can't select multiple choice in config " + configId);
            }
            var variantIdsEntity = productVariantMap.getOrDefault(configId, Collections.emptySet());
            for (var variantId : variantIds) {
                if (!variantIdsEntity.contains(variantId)) {
                    throw new InvalidException("Product variant " + variantId + " not found");
                }
            }
        }
    }
}
