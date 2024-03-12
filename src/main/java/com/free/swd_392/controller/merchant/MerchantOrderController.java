package com.free.swd_392.controller.merchant;

import com.free.swd_392.controller.BaseController;
import com.free.swd_392.core.controller.IGetDetailsController;
import com.free.swd_392.core.controller.IGetInfoPageWithFilterController;
import com.free.swd_392.core.model.BasePagingResponse;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.core.model.SuccessResponse;
import com.free.swd_392.dto.merchant.request.filter.MerchantOrderInfoPageFilter;
import com.free.swd_392.dto.order.OrderInfo;
import com.free.swd_392.dto.order.request.BusinessPerformanceRequest;
import com.free.swd_392.dto.order.request.MerchantChangeOrderStatusRequest;
import com.free.swd_392.entity.order.OrderEntity;
import com.free.swd_392.enums.OrderStatus;
import com.free.swd_392.exception.InvalidException;
import com.free.swd_392.mapper.app.AppOrderMapper;
import com.free.swd_392.repository.order.OrderRepository;
import com.free.swd_392.shared.projection.IBusinessPerformanceProjection;
import com.free.swd_392.shared.projection.IRevenueThisAndLastMonthProjection;
import com.free.swd_392.shared.projection.OrderInfoProjection;
import com.free.swd_392.shared.utils.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Tag(name = "Merchant Order Controller")
@RestController
@RequestMapping("/api/v1/merchant/order")
@RequiredArgsConstructor
public class MerchantOrderController extends BaseController implements
        IGetInfoPageWithFilterController<UUID, OrderInfo, UUID, OrderEntity, MerchantOrderInfoPageFilter>,
        IGetDetailsController<UUID, OrderInfo, UUID, OrderEntity> {

    private final OrderRepository repository;
    private final AppOrderMapper appOrderMapper;

    @GetMapping("/business-performance")
    public ResponseEntity<BaseResponse<IBusinessPerformanceProjection>> businessPerformance(@Valid @ParameterObject BusinessPerformanceRequest request) {
        return wrapResponse(repository.getBusinessPerformanceInDay(request.getDate()));
    }

    @GetMapping("/revenue-by-month")
    public ResponseEntity<BaseResponse<List<IRevenueThisAndLastMonthProjection>>> revenueByThisAndLastMonth() {
        return wrapResponse(repository.getRevenueByThisAndLastMonth());
    }

    @Transactional
    @PatchMapping("/change-status")
    public ResponseEntity<SuccessResponse> changeStatus(@Valid @RequestBody MerchantChangeOrderStatusRequest request) {
        var order = findById(request.getId(), notFound());
        if (!Objects.equals(order.getMerchantId(), JwtUtils.getMerchantId())) {
            throw new AccessDeniedException("");
        }
        if (!OrderStatus.validateNextState(order.getStatus(), request.getStatus())) {
            throw new InvalidException("Invalid state, available: " + OrderStatus.getAvailableStates(order.getStatus()));
        }
        order.setStatus(request.getStatus());
        repository.save(order);
        return success();
    }

    @Override
    public BasePagingResponse<OrderInfo> aroundGetPageInfoWithFilter(MerchantOrderInfoPageFilter filter) {
        filter.setMerchantId(JwtUtils.getMerchantId());
        Page<OrderInfoProjection> pageEntity = repository.findByFilters(filter, filter.getPageable());
        return new BasePagingResponse<>(pageEntity, appOrderMapper::convertToInfoProjectionList);
    }

    @Override
    public OrderInfo aroundGetDetails(UUID id) throws InvalidException {
        var order = repository.findOne((r, q, b) -> {
                    r.fetch(OrderEntity.Fields.province);
                    r.fetch(OrderEntity.Fields.district);
                    r.fetch(OrderEntity.Fields.ward);
                    return b.and(
                            b.equal(r.get(OrderEntity.Fields.id), id),
                            b.equal(r.get(OrderEntity.Fields.merchantId), JwtUtils.getMerchantId())
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
    public OrderInfo convertToDetails(OrderEntity entity) {
        return appOrderMapper.convertToDetails(entity);
    }

    @Override
    public JpaRepository<OrderEntity, UUID> getRepository() {
        return repository;
    }
}
