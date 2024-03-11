package com.free.swd_392.controller.system;

import com.free.swd_392.controller.BaseController;
import com.free.swd_392.core.controller.IGetDetailsController;
import com.free.swd_392.core.controller.IGetInfoPageWithFilterController;
import com.free.swd_392.core.model.SuccessResponse;
import com.free.swd_392.dto.merchant.MerchantDetails;
import com.free.swd_392.dto.merchant.MerchantInfo;
import com.free.swd_392.dto.merchant.request.SystemApproveOrRejectMerchantRequest;
import com.free.swd_392.dto.merchant.request.filter.SystemMerchantInfoPageFilter;
import com.free.swd_392.entity.merchant.MerchantEntity;
import com.free.swd_392.enums.MerchantStatus;
import com.free.swd_392.enums.RoleKind;
import com.free.swd_392.mapper.system.SystemMerchantMapper;
import com.free.swd_392.repository.merchant.MerchantRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@Tag(name = "System Merchant Controller")
@RestController
@RequestMapping("/api/v1/system/merchant")
@RequiredArgsConstructor
public class SystemMerchantController extends BaseController implements
        IGetInfoPageWithFilterController<Long, MerchantInfo, Long, MerchantEntity, SystemMerchantInfoPageFilter>,
        IGetDetailsController<Long, MerchantDetails, Long, MerchantEntity> {

    private final MerchantRepository repository;
    private final SystemMerchantMapper systemMerchantMapper;
    private final FirebaseAuth firebaseAuth;

    @PostMapping("/change-status")
    @Transactional
    public ResponseEntity<SuccessResponse> approveOrRejectMerchant(@Valid @RequestBody SystemApproveOrRejectMerchantRequest request) throws FirebaseAuthException {
        var merchantEntity = findById(request.getMerchantId(), notFound());
        merchantEntity.setStatus(request.getStatus());
        var listRole = new ArrayList<String>();
        listRole.add(RoleKind.USER_VALUE);
        if (MerchantStatus.ACTIVE.equals(request.getStatus())) {
            listRole.add(RoleKind.MERCHANT_VALUE);
        }
        firebaseAuth.setCustomUserClaims(
                merchantEntity.getOwnerId(),
                Map.of("resource_access", Map.of(
                                "auction", Map.of(
                                        "roles",
                                        listRole
                                )
                        ),
                        "merchant_id", merchantEntity.getId()
                )
        );
        merchantEntity.setApprovalReason(request.getReason());
        repository.save(merchantEntity);
        return success();
    }

    @Override
    public MerchantDetails convertToDetails(MerchantEntity entity) {
        return systemMerchantMapper.convertToDetails(entity);
    }

    @Override
    public MerchantInfo convertToInfo(MerchantEntity entity) {
        return systemMerchantMapper.convertToInfo(entity);
    }

    @Override
    public JpaRepository<MerchantEntity, Long> getRepository() {
        return repository;
    }

    @Override
    public String notFound() {
        return "Merchant not found";
    }
}
