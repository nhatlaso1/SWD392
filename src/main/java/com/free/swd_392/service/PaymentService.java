package com.free.swd_392.service;

import com.free.swd_392.config.client.currency.CurrencyDSGateway;
import com.free.swd_392.config.client.paypal.PayPalClient;
import com.free.swd_392.config.client.paypal.common.*;
import com.free.swd_392.config.client.paypal.request.CreateOrderRequest;
import com.free.swd_392.entity.order.OrderEntity;
import com.free.swd_392.entity.payment.PaymentEntity;
import com.free.swd_392.enums.PaymentStatus;
import com.free.swd_392.enums.PaymentType;
import com.free.swd_392.exception.InvalidException;
import com.free.swd_392.repository.merchant.MerchantRepository;
import com.free.swd_392.repository.order.OrderRepository;
import com.free.swd_392.repository.payment.PaymentRepository;
import com.free.swd_392.repository.product.SkuRepository;
import com.free.swd_392.shared.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.free.swd_392.config.client.currency.CurrencyCode.USD;
import static com.free.swd_392.config.client.currency.CurrencyCode.VND;
import static com.free.swd_392.shared.constant.PaymentAction.CANCEL;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PayPalClient payPalClient;
    private final CurrencyDSGateway currencyDSGateway;
    private final PaymentRepository paymentRepository;
    private final MerchantRepository merchantRepository;
    private final OrderRepository orderRepository;
    private final SkuRepository skuRepository;

    @Value("${paypal.approve-url}")
    private final String approveUrl;
    @Value("${paypal.cancel-url}")
    private final String cancelUrl;

    public String createUrlByOrder(List<OrderEntity> orders) {
        var payment = new PaymentEntity()
                .setId(UUID.randomUUID())
                .setStatus(PaymentStatus.PENDING)
                .setPaymentType(PaymentType.PAYMENT);
        if (CollectionUtils.isEmpty(orders)) {
            throw new InvalidException("Orders can not be empty");
        }
        var approveFullUrl = UriComponentsBuilder.fromHttpUrl(approveUrl)
                .path(payment.getId().toString())
                .build();
        var cancelFullUrl = UriComponentsBuilder.fromHttpUrl(cancelUrl)
                .path(payment.getId().toString())
                .build();
        var firstOrder = orders.get(0);
        var merchant = merchantRepository.findById(firstOrder.getMerchantId())
                .orElseThrow(() -> new InvalidException("Merchant not found"));
        var currency = currencyDSGateway.getByCode(USD);
        var subTotalPrice = BigDecimal.ZERO;
        var discount = BigDecimal.ZERO;
        var shipping = BigDecimal.ZERO;
        StringBuilder noteBuilder = new StringBuilder();
        PurchaseUnit.PurchaseUnitBuilder purchaseUnitBuilder = PurchaseUnit.builder();
        for (var order : orders) {
            for (var item : order.getOrderItems()) {
                Money unitAmount = currency.convert(item.getPrice(), VND, USD);
                purchaseUnitBuilder.item(Item.builder()
                        .name(item.productFullName())
                        .quantity(item.getQuantity())
                        .description(item.getNote())
                        .unitAmount(unitAmount)
                        .build()
                );
                subTotalPrice = subTotalPrice.add(BigDecimal.valueOf(unitAmount.getValue() * item.getQuantity()));
            }
            discount = discount.add(order.getDiscount());
            shipping = shipping.add(order.getShippingCharge());
            noteBuilder.append(order.getDescription())
                    .append("\n");
            order.setPaymentId(payment.getId());
        }
        purchaseUnitBuilder
                .referenceId(payment.getId().toString())
                .description(noteBuilder.toString())
                .customId(JwtUtils.getUserId())
                .amount(Amount.builder()
                        .currencyCode(USD)
                        .breakdown(Amount.Breakdown.builder()
                                .itemTotal(new Money(subTotalPrice.doubleValue(), USD))
                                .shipping(currency.convert(shipping, VND, USD))
                                .discount(currency.convert(discount, VND, USD))
                                .build()
                        )
                        .build()
                )
                .shipping(Shipping.builder()
                        .type(ShippingType.SHIPPING)
                        .name(Shipping.Name.builder().surName(firstOrder.getReceiverFullName()).build())
                        // .address(Shipping.Address.builder().addressLine1(firstOrder.addressLine()).build())
                        .build()
                );
        CreateOrderRequest orderRequest = CreateOrderRequest.builder()
                .intent(OrderIntent.CAPTURE)
                .purchaseUnit(purchaseUnitBuilder.build())
                .applicationContext(PayPalAppContext.builder()
                        .brandName(merchant.getName())
                        .returnUrl(approveFullUrl.toString())
                        .cancelUrl(cancelFullUrl.toString())
                        .landingPage(PaymentLandingPage.BILLING)
                        .build()
                )
                .build();
        var orderResponse = payPalClient.createOrder(orderRequest);
        payment.setRefTransactionId(orderResponse.getId());
        paymentRepository.save(payment);
        orderRepository.saveAll(orders);
        return orderResponse.getApproveLink();
    }

    @Transactional
    public void confirm(String action, UUID paymentId) {
        var payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new InvalidException("Payment not found"));
        if (CANCEL.equals(action)) {
            paymentRepository.delete(payment);
            return;
        }
        var orderResponse = payPalClient.captureOrder(payment.getRefTransactionId());
        if (orderResponse == null || !OrderStatus.COMPLETED.equals(orderResponse.getStatus())) {
            throw new InvalidException("Order not complete");
        }
        payment.setPaymentData(orderResponse);
        payment.setStatus(PaymentStatus.SUCCESS);
        var orders = orderRepository.findAllByPaymentId(paymentId);
        for (var order : orders) {
            order.setStatus(com.free.swd_392.enums.OrderStatus.PAID);
        }
        orderRepository.saveAll(orders);
    }
}
