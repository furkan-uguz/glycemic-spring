package com.glycemic.services.glycemic.service;

import com.glycemic.core.config.AuditAwareConfigurer;
import com.glycemic.core.model.MainResponse;
import com.glycemic.entity.model.glycemic.Food;
import com.glycemic.entity.repository.glycemic.FoodRepository;
import com.glycemic.services.glycemic.model.food.list.FoodListResponse;
import com.glycemic.services.glycemic.util.EFoodStatus;
import com.glycemic.services.glycemic.util.EGlycemicErrorTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final FoodRepository foodRepo;

    private final AuditAwareConfigurer auditAwareConfig;

    @Value("${app.pageElementSize}")
    private Integer pageSize;

    public MainResponse searchWith(String q, String category, int page) {
        MainResponse response = new MainResponse();
        FoodListResponse result = new FoodListResponse();

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.NOT_FOUND.ordinal());
        response.setMessage("Sonuç bulunamadı.");
        response.setResult(result);

        Page<Food> foods;
        page = page <= 0 ? 1 : page;

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        if (category.equals("all"))
            foods = foodRepo.foodsNameWithAll(q, EFoodStatus.ACCEPT.ordinal(), pageable);
        else
            foods = foodRepo.foodsNameWithCategoryJoinAndLimited(q, category, EFoodStatus.ACCEPT.ordinal(), pageable);

        if (!foods.isEmpty()) {
            result.setFoodList(foods.getContent());
            result.setPage(page);
            result.setTotalElements(foods.getTotalElements());
            result.setTotalPage(foods.getTotalPages());

            response.setStatus(true);
            response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
            response.setMessage("Sonuç(lar) bulundu.");
            response.setResult(result);
        }
        return response;
    }

    public MainResponse searchWithUser(String q, String category, int page) {
        MainResponse response = new MainResponse();
        FoodListResponse result = new FoodListResponse();

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.NOT_FOUND.ordinal());
        response.setMessage("Sonuç bulunamadı.");
        response.setResult(result);

        Optional<String> oUserName = auditAwareConfig.getCurrentAuditor();

        if (oUserName.isPresent()) {
            Page<Food> foods;
            page = page <= 0 ? 1 : page;

            Pageable pageable = PageRequest.of(page - 1, pageSize);

            if (category.equals("all"))
                foods = foodRepo.foodsNameWithAllForUser(q, oUserName.get(), pageable);
            else
                foods = foodRepo.foodsNameWithCategoryJoinAndLimitedForUser(q, category, oUserName.get(), pageable);

            if (!foods.isEmpty()) {
                result.setFoodList(foods.getContent());
                result.setPage(page);
                result.setTotalElements(foods.getTotalElements());
                result.setTotalPage(foods.getTotalPages());

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Sonuç(lar) bulundu.");
                response.setResult(result);
            }
        }
        return response;
    }
}
