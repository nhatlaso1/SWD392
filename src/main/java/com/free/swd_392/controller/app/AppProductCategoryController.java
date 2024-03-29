package com.free.swd_392.controller.app;

import com.free.swd_392.controller.BaseController;
import com.free.swd_392.core.model.BaseResponse;
import com.free.swd_392.dto.product.ProductCategoryInfo;
import com.free.swd_392.mapper.app.AppProductCategoryMapper;
import com.free.swd_392.repository.product.ProductCategoryRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Tag(name = "App Product Category Controller")
@RestController
@RequestMapping("/api/v1/app/product-category")
@RequiredArgsConstructor
public class AppProductCategoryController extends BaseController {

    private final ProductCategoryRepository repository;
    private final AppProductCategoryMapper appProductCategoryMapper;

    @GetMapping("/public/list")
    public ResponseEntity<BaseResponse<List<ProductCategoryInfo>>> getInfoList() {
        var categoryInfoList = repository.findAllCategoryActivatingRecursive();
        var categoryInfoMap = categoryInfoList.stream()
                .collect(Collectors.toMap(
                        ProductCategoryInfo::getId,
                        Function.identity(),
                        (o1, o2) -> o1,
                        HashMap::new
                ));
        for (var categoryEntity : categoryInfoList) {
            var categoryParentInfo = categoryInfoMap.get(categoryEntity.getParentId());
            if (categoryParentInfo != null) {
                var categoryInfo = categoryInfoMap.get(categoryEntity.getId());
                categoryParentInfo.addChild(categoryInfo);
            }
        }
        return wrapResponse(
                categoryInfoMap.values().stream()
                        .filter(pc -> pc.getParentId() == null)
                        .toList()
        );
    }
}
