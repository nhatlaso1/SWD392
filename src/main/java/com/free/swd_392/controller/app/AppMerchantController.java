package com.free.swd_392.controller.app;

import com.free.swd_392.controller.BaseController;
import com.free.swd_392.core.controller.ICreateModelController;
import com.free.swd_392.core.controller.IGetDetailsByContextController;
import com.free.swd_392.core.controller.IGetInfoController;
import com.free.swd_392.core.controller.IUpdateModelController;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.core.model.SuccessResponse;
import com.free.swd_392.dto.merchant.MerchantDetails;
import com.free.swd_392.dto.merchant.MerchantInfo;
import com.free.swd_392.dto.merchant.request.AppChangeStatusMerchantRequest;
import com.free.swd_392.dto.merchant.request.AppCreateMerchantRequest;
import com.free.swd_392.dto.merchant.request.AppUpdateMerchantRequest;
import com.free.swd_392.entity.merchant.MerchantEntity;
import com.free.swd_392.enums.MerchantStatus;
import com.free.swd_392.exception.InvalidException;
import com.free.swd_392.mapper.app.AppMerchantMapper;
import com.free.swd_392.repository.location.LocationRepository;
import com.free.swd_392.repository.merchant.MerchantRepository;
import com.free.swd_392.service.PermissionService;
import com.free.swd_392.shared.utils.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@Tag(name = "App Merchant Controller")
@RestController
@RequestMapping("/api/v1/app/merchant")
@RequiredArgsConstructor
public class AppMerchantController extends BaseController implements
        ICreateModelController<Long, MerchantDetails, AppCreateMerchantRequest, Long, MerchantEntity>,
        IUpdateModelController<Long, MerchantDetails, AppUpdateMerchantRequest, Long, MerchantEntity>,
        IGetInfoController<Long, MerchantInfo, Long, MerchantEntity>,
        IGetDetailsByContextController<Long, MerchantDetails, Long, MerchantEntity> {

    private final MerchantRepository repository;
    private final LocationRepository locationRepository;
    private final AppMerchantMapper appMerchantMapper;
    private final PermissionService permissionService;

    @Override
    @PostMapping("/draft/save")
    @Transactional
    public ResponseEntity<BaseResponse<MerchantDetails>> createModel(@RequestBody @Valid AppCreateMerchantRequest request) {
        return success(aroundCreate(preCreate(request)));
    }

    @Override
    public AppCreateMerchantRequest preCreate(AppCreateMerchantRequest request) {
        if (!locationRepository.existsLocationPath(request.getProvinceId(), request.getDistrictId(), request.getWardId())) {
            throw new InvalidException("Invalid location path");
        }
        request.setOwnerId(JwtUtils.getUserId());
        request.setStatus(MerchantStatus.DRAFT);
        return request;
    }

    @Override
    public MerchantDetails aroundCreate(AppCreateMerchantRequest request) {
        var merchantEntity = repository.findByOwnerId(request.getOwnerId())
                .orElse(null);
        if (merchantEntity == null) {
            return ICreateModelController.super.aroundCreate(request);
        }
        final var currentState = merchantEntity.getStatus();
        if (Stream.of(MerchantStatus.DRAFT, MerchantStatus.IN_REVIEW)
                .noneMatch(s -> s.equals(currentState))) {
            throw new InvalidException("Invalid state of merchant");
        }
        appMerchantMapper.updateDraft(merchantEntity, request);
        merchantEntity = repository.save(merchantEntity);
        if (returnResultAfterCreate()) {
            return convertToDetails(merchantEntity);
        }
        return null;
    }

    @PatchMapping("/draft/submit")
    @Transactional
    public ResponseEntity<SuccessResponse> submitDraft() {
        var merchantEntity = findByOwnerId();
        merchantEntity.setStatus(MerchantStatus.IN_REVIEW);
        repository.save(merchantEntity);
        return success();
    }

    @PatchMapping("/change-status")
    @Transactional
    public ResponseEntity<SuccessResponse> changeStatus(@Valid @RequestBody AppChangeStatusMerchantRequest request) {
        var merchantEntity = findByOwnerId();
        final var status = merchantEntity.getStatus();
        if (Stream.of(MerchantStatus.ACTIVE, MerchantStatus.INACTIVE)
                .noneMatch(s -> s.equals(status))) {
            throw new InvalidException("Invalid state of merchant");
        }
        merchantEntity.setStatus(request.getStatus());
        repository.save(merchantEntity);
        return success();
    }

    @Override
    @Transactional
    @GetMapping("/public/{id}/info")
    public ResponseEntity<BaseResponse<MerchantInfo>> getInfoById(@PathVariable("id") @NotNull Long id) {
        var merchant = repository.findByIdAndStatus(id, MerchantStatus.ACTIVE)
                .orElseThrow(() -> new InvalidException(notFound()));
        return success(convertToInfo(merchant));
    }

    @Override
    public AppUpdateMerchantRequest preUpdate(AppUpdateMerchantRequest request) {
        var merchantEntity = findByOwnerId();
        permissionService.check(merchantEntity);
        if (!MerchantStatus.ACTIVE.equals(merchantEntity.getStatus())) {
            throw new InvalidException("You must be approved to use this function");
        }
        request.setId(merchantEntity.getId());
        return request;
    }

    @Override
    public ResponseEntity<BaseResponse<MerchantDetails>> getDetailsByContext() {
        return success(convertToDetails(findByOwnerId()));
    }

    @Override
    public MerchantDetails convertToDetails(MerchantEntity entity) {
        return appMerchantMapper.convertToDetails(entity);
    }

    @Override
    public MerchantInfo convertToInfo(MerchantEntity entity) {
        return appMerchantMapper.convertToInfo(entity);
    }

    @Override
    public MerchantEntity createConvertToEntity(AppCreateMerchantRequest request) {
        return appMerchantMapper.createConvertToEntity(request);
    }

    @Override
    public void updateConvertToEntity(MerchantEntity entity, AppUpdateMerchantRequest request) {
        appMerchantMapper.updateConvertToEntity(entity, request);
    }

    @Override
    public JpaRepository<MerchantEntity, Long> getRepository() {
        return repository;
    }

    @Override
    public String notFound() {
        return "Merchant not found";
    }

    private MerchantEntity findByOwnerId() {
        return repository.findByOwnerId(JwtUtils.getUserId())
                .orElseThrow(() -> new InvalidException(notFound()));
    }
}
