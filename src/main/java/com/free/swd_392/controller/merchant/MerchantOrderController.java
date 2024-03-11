package com.free.swd_392.controller.merchant;

import com.free.swd_392.controller.BaseController;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.dto.order.request.BusinessPerformanceRequest;
import com.free.swd_392.repository.order.OrderRepository;
import com.free.swd_392.shared.projection.IBusinessPerformanceProjection;
import com.free.swd_392.shared.projection.IRevenueThisAndLastMonthProjection;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "Merchant Order Controller")
@RestController
@RequestMapping("/api/v1/merchant/order")
@RequiredArgsConstructor
public class MerchantOrderController extends BaseController {

    private final OrderRepository orderRepository;

    @GetMapping("/business-performance")
    public ResponseEntity<BaseResponse<IBusinessPerformanceProjection>> businessPerformance(@Valid @ParameterObject BusinessPerformanceRequest request) {
        return wrapResponse(orderRepository.getBusinessPerformanceInDay(request.getDate()));
    }

    @GetMapping(value = "/revenue-by-month")
    public ResponseEntity<BaseResponse<List<IRevenueThisAndLastMonthProjection>>> revenueByThisAndLastMonth() {
        return wrapResponse(orderRepository.getRevenueByThisAndLastMonth());
    }
}
