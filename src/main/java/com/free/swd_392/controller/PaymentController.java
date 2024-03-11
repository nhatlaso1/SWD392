package com.free.swd_392.controller;

import com.free.swd_392.exception.InvalidException;
import com.free.swd_392.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;
import java.util.stream.Stream;

import static com.free.swd_392.shared.constant.PaymentAction.APPROVE;
import static com.free.swd_392.shared.constant.PaymentAction.CANCEL;

@Slf4j
@Tag(name = "Payment Controller")
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    @Value("${paypal.app-redirect-url}")
    private final String appRedirectUrl;

    @GetMapping("/checkout/{action}/{paymentId}")
    public void confirmCheckedOut(@PathVariable("action") @NotNull String action,
                                  @PathVariable("paymentId") @NotNull UUID paymentId,
                                  @RequestParam(required = false) String token,
                                  @RequestParam(value = "PayerId", required = false) String payerId,
                                  HttpServletResponse response) {
        if (Stream.of(APPROVE, CANCEL).noneMatch(s -> s.equals(action))) {
            throw new InvalidException("Invalid payment action");
        }
        paymentService.confirm(action, paymentId);
        var uriFE = UriComponentsBuilder.fromHttpUrl(appRedirectUrl)
                .queryParam("action", action)
                .queryParam("paymentId", paymentId.toString())
                .build();
        response.setHeader(HttpHeaders.LOCATION, uriFE.toString());
        response.setStatus(HttpStatus.SEE_OTHER.value());
    }
}
