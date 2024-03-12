package com.free.swd_392.controller.app;

import com.free.swd_392.controller.BaseController;
import com.free.swd_392.core.controller.IGetDetailsController;
import com.free.swd_392.core.controller.IGetInfoPageWithFilterController;
import com.free.swd_392.core.model.BasePagingResponse;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.dto.order.OrderDetails;
import com.free.swd_392.dto.order.OrderInfo;
import com.free.swd_392.dto.order.request.CreateOrderItemRequest;
import com.free.swd_392.dto.order.request.CreateOrderRequest;
import com.free.swd_392.dto.order.request.filter.AppUserOrderPageFilter;
import com.free.swd_392.dto.order.response.CreateOrderResponse;
import com.free.swd_392.entity.audit.Audit;
import com.free.swd_392.entity.order.OrderEntity;
import com.free.swd_392.entity.order.OrderItemEntity;
import com.free.swd_392.entity.product.SkuEntity;
import com.free.swd_392.enums.OrderStatus;
import com.free.swd_392.exception.InvalidException;
import com.free.swd_392.mapper.app.AppOrderMapper;
import com.free.swd_392.repository.order.OrderRepository;
import com.free.swd_392.repository.product.ProductVariantRepository;
import com.free.swd_392.repository.product.SkuRepository;
import com.free.swd_392.repository.user.UserAddressRepository;
import com.free.swd_392.service.PaymentService;
import com.free.swd_392.shared.model.order.OrderExtraVariant;
import com.free.swd_392.shared.projection.OrderInfoProjection;
import com.free.swd_392.shared.utils.JwtUtils;
import com.google.common.collect.Sets;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "App Order Controller")
@RestController
@RequestMapping("/api/v1/app/order")
@RequiredArgsConstructor
public class AppOrderController extends BaseController implements
        IGetInfoPageWithFilterController<UUID, OrderInfo, UUID, OrderEntity, AppUserOrderPageFilter>,
        IGetDetailsController<UUID, OrderDetails, UUID, OrderEntity> {

    private final OrderRepository repository;
    private final UserAddressRepository userAddressRepository;
    private final ProductVariantRepository productVariantRepository;
    private final SkuRepository skuRepository;
    private final AppOrderMapper appOrderMapper;
    private final PaymentService paymentService;

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<BaseResponse<CreateOrderResponse>> createModel(@Valid @RequestBody CreateOrderRequest request) {
        var requestSkuIds = request.getOrderItemMap().keySet();
        if (requestSkuIds.size() != request.getOrderItems().size()) {
            throw new InvalidException("Duplicate sku id");
        }
        var skus = skuRepository.findByIdsFetchJoinProduct(
                request.getOrderItems().stream()
                        .map(CreateOrderItemRequest::getSkuId)
                        .toList()
        );
        var idsNotExists = Sets.difference(requestSkuIds, skus.stream().map(SkuEntity::getId).collect(Collectors.toSet()));
        if (!idsNotExists.isEmpty()) {
            throw new InvalidException("Sku ids not found: " + idsNotExists);
        }
        var userAddress = userAddressRepository.findById(request.getUserAddressId())
                .orElseThrow(() -> new InvalidException("User address not found"));
        Map<Long, OrderEntity> merchantIdOrderMap = new HashMap<>();
        for (var sku : skus) {
            if (Objects.equals(sku.getProduct().getMerchantId(), JwtUtils.getMerchantId())) {
                throw new InvalidException("Can not self buying product");
            }
            var order = merchantIdOrderMap.compute(sku.getProduct().getMerchantId(), (k, o) -> {
                if (o == null) {
                    o = new OrderEntity();
                    o.mapUserAddress(userAddress);
                    o.setDescription(request.getDescription());
                    o.setPaymentMethod(request.getPaymentMethod());
                    o.setStatus(OrderStatus.NEW);
                    o.setMerchantId(sku.getProduct().getMerchantId());
                }
                return o;
            });
            var requestItem = request.getOrderItemMap().get(sku.getId());
            var orderItem = new OrderItemEntity();
            orderItem.setProductId(sku.getProduct().getId());
            orderItem.setProductName(sku.getProduct().getName());
            orderItem.setPrice(sku.getPrice());
            orderItem.setDiscount(BigDecimal.ZERO);
            orderItem.setQuantity(requestItem.getQuantity());
            orderItem.setNote(requestItem.getNote());
            orderItem.setExtraVariants(toOrderExtraVariants(sku));
            order.addOrderItem(orderItem);
        }
        var orderSaved = repository.saveAll(merchantIdOrderMap.values());
        var response = new CreateOrderResponse();
        response.setPaymentUrl(paymentService.createUrlByOrder(orderSaved));
        return success(response);
    }

    @GetMapping("/create-payment/{orderId}")
    @Transactional
    public ResponseEntity<BaseResponse<CreateOrderResponse>> getPaymentUrl(@PathVariable @NotNull UUID orderId) {
        var order = findById(orderId, notFound());
        var response = new CreateOrderResponse();
        response.setPaymentUrl(paymentService.createUrlByOrder(List.of(order)));
        return success(response);
    }

    @Override
    public BasePagingResponse<OrderInfo> aroundGetPageInfoWithFilter(AppUserOrderPageFilter filter) {
        filter.setUserId(JwtUtils.getUserId());
        Page<OrderInfoProjection> pageEntity = repository.findByFilters(filter, filter.getPageable());
        return new BasePagingResponse<>(pageEntity, appOrderMapper::convertToInfoProjectionList);
    }

    @Override
    public OrderDetails aroundGetDetails(UUID id) throws InvalidException {
        var order = repository.findOne((r, q, b) -> {
                    r.fetch(OrderEntity.Fields.province);
                    r.fetch(OrderEntity.Fields.district);
                    r.fetch(OrderEntity.Fields.ward);
                    return b.and(
                            b.equal(r.get(OrderEntity.Fields.id), id),
                            b.equal(r.get(Audit.Fields.createdBy), JwtUtils.getUserId())
                    );
                })
                .orElseThrow(() -> new InvalidException(notFound()));
        return convertToDetails(order);
    }

    @Override
    public OrderInfo convertToInfo(OrderEntity entity) {
        return appOrderMapper.convertToInfo(entity);
    }

    @Override
    public OrderDetails convertToDetails(OrderEntity entity) {
        return appOrderMapper.convertToDetails(entity);
    }

    @Override
    public JpaRepository<OrderEntity, UUID> getRepository() {
        return repository;
    }

    @Override
    public String notFound() {
        return "Order not found";
    }

    private List<OrderExtraVariant> toOrderExtraVariants(SkuEntity sku) {
        var configTree = productVariantRepository.findVariantsBySku(sku.getId());
        Map<Long, OrderExtraVariant> exMap = new HashMap<>();
        List<OrderExtraVariant> result = new LinkedList<>();
        configTree.forEach(variant -> {
            var extra = exMap.compute(variant.getConfig().getId(), (k, v) -> {
                if (v == null) {
                    v = new OrderExtraVariant();
                    v.setName(variant.getConfig().getName());
                    v.setImage(sku.getImage());
                    result.add(v);
                }
                return v;
            });
            extra.addVariant(variant.getName());
        });
        return result;
    }
}
