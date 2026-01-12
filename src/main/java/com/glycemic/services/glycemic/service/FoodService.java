package com.glycemic.services.glycemic.service;

import com.glycemic.core.config.AuditAwareConfigurer;
import com.glycemic.core.model.MainResponse;
import com.glycemic.core.util.Generator;
import com.glycemic.entity.model.glycemic.Food;
import com.glycemic.entity.model.glycemic.FoodNutritional;
import com.glycemic.entity.model.glycemic.Nutritional;
import com.glycemic.entity.repository.glycemic.FoodNutritionalRepository;
import com.glycemic.entity.repository.glycemic.FoodRepository;
import com.glycemic.entity.repository.glycemic.NutritionalRepository;
import com.glycemic.services.glycemic.model.food.cud.FoodCUDResponse;
import com.glycemic.services.glycemic.model.food.from.FoodResponse;
import com.glycemic.services.glycemic.model.food.list.FoodListResponse;
import com.glycemic.services.glycemic.util.EFoodListStatus;
import com.glycemic.services.glycemic.util.EFoodStatus;
import com.glycemic.services.glycemic.util.EGlycemicErrorTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepo;

    private final FoodNutritionalRepository fNutrRepo;

    private final NutritionalRepository nutriRepo;

    private final AuditAwareConfigurer auditAwareConfig;

    @Value("${app.pageElementSize}")
    private Integer pageSize;

    public MainResponse foodList() {
        MainResponse response = new MainResponse();
        FoodListResponse result = new FoodListResponse();

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.NOT_FOUND.ordinal());
        response.setMessage("Sonuç bulunamadı.");
        response.setResult(result);

        try {
            Pageable pageable = PageRequest.of(0, pageSize);
            Page<Food> foods = foodRepo.findAllPageable(EFoodStatus.ACCEPT.ordinal(), pageable);

            if (!foods.isEmpty()) {
                result.setFoodList(foods.getContent());
                result.setPage(1);
                result.setTotalElements(foods.getTotalElements());
                result.setTotalPage(foods.getTotalPages());

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Sonuç(lar) bulundu.");
                response.setResult(result);
            }
        } catch (Exception e) {
            log.error("An error occurred when getting food list.", e);
            response.setMessage("Error: Foods is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MainResponse userFoodList() {
        MainResponse response = new MainResponse();
        FoodListResponse result = new FoodListResponse();

        Optional<String> oUserName = auditAwareConfig.getCurrentAuditor();

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.NOT_FOUND.ordinal());
        response.setMessage("Sonuç bulunamadı.");
        response.setResult(result);

        try {
            if (oUserName.isPresent()) {
                Pageable pageable = PageRequest.of(0, pageSize);
                Page<Food> foods = foodRepo.findByCreatedByWithPage(oUserName.get(), pageable);

                result.setFoodList(foods.getContent());
                result.setPage(1);
                result.setTotalElements(foods.getTotalElements());
                result.setTotalPage(foods.getTotalPages());

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Sonuç(lar) bulundu.");
                response.setResult(result);
            }
        } catch (Exception e) {
            log.error("An error occurred when getting user food list.", e);
            response.setMessage("Error: User foods is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Transactional("transactionManager")
    public MainResponse insert(Food foods) {
        MainResponse response = new MainResponse();
        FoodCUDResponse result = new FoodCUDResponse();

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.INSERTED_BEFORE.ordinal());
        response.setMessage("Bu sonuç daha önce eklendi.");
        response.setResult(result);

        try {
            Optional<Food> oFoods = foodRepo.findByNameEqualsIgnoreCase(foods.getName());

            if (oFoods.isEmpty()) {
                foods.setId(0L);
                foods.setEnabled(false);
                foods.setUrl(Generator.generateUrl(foods.getName()));
                foods.setFoodStatus(EFoodStatus.WAITING);
                Food food = foodRepo.save(foods);

                if (!foods.getFoodNutritional().isEmpty()) {
                    food.setFoodNutritional(getFoodNutritionals(foods, food));
                }
                result.setFood(food);

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Sonuç(lar) eklendi.");
                response.setResult(result);
            }
        } catch (Exception e) {
            log.error("An error occurred when insert food.", e);
            response.setMessage("Error: Insert foods is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Transactional("transactionManager")
    public MainResponse delete(Long id) {
        MainResponse response = new MainResponse();
        FoodCUDResponse result = new FoodCUDResponse();

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.NOT_FOUND.ordinal());
        response.setMessage("Sonuç bulunamadı.");
        response.setResult(result);

        try {
            Optional<Food> food = foodRepo.findById(id);

            if (food.isPresent()) {
                foodRepo.delete(food.get());

                result.setFood(food.get());

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Sonuç(lar) temizlendi.");
                response.setResult(result);
            }
        } catch (Exception e) {
            log.error("An error occurred when delete food.", e);
            response.setMessage("Error: Delete foods is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MainResponse update(Food updateFood) {
        MainResponse response = new MainResponse();
        FoodCUDResponse result = new FoodCUDResponse();

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.NOT_FOUND.ordinal());
        response.setMessage("Sonuç bulunamadı.");
        response.setResult(result);

        try {
            Optional<Food> food = foodRepo.findById(updateFood.getId());

            if (food.isPresent()) {
                Optional<String> oUserName = auditAwareConfig.getCurrentAuditor();

                if (!auditAwareConfig.isAdminUser() && (oUserName.isPresent() && !food.get().getCreatedBy().equals(oUserName.get()))) {
                    response.setErrorCode(EGlycemicErrorTypes.NOT_AUTHORIZED.ordinal());
                    response.setMessage("Yetkiniz olmayan bir ürünü güncelleyemezsiniz.");
                } else {
                    updateFood.setEnabled(false);
                    updateFood.setFoodStatus(EFoodStatus.WAITING);
                    updateFood.setUrl(Generator.generateUrl(updateFood.getName()));
                    updateFood.setCreatedBy(food.get().getCreatedBy());
                    updateFood.setCreatedDate(food.get().getCreatedDate());

                    Food updated = foodRepo.save(updateFood);

                    result.setFood(updated);

                    response.setStatus(true);
                    response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                    response.setMessage("Sonuç(lar) güncellendi.");
                    response.setResult(result);
                }
            }
        } catch (Exception e) {
            log.error("An error occurred when update food.", e);
            response.setMessage("Error: Update foods is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MainResponse check(Long id) {
        MainResponse response = new MainResponse();
        FoodResponse result = new FoodResponse();

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.NOT_FOUND.ordinal());
        response.setMessage("Sonuç bulunamadı.");
        response.setResult(result);

        try {
            Optional<Food> food = foodRepo.findById(id);

            if (food.isPresent()) {
                List<FoodNutritional> foodNutritionalList = fNutrRepo.findAllByFood(food.get());
                Food cloneFood = food.get().copy();
                cloneFood.setFoodNutritional(foodNutritionalList);

                result.setFood(cloneFood);

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Ürün doğrulandı.");
                response.setResult(result);
            }
        } catch (Exception e) {
            log.error("An error occurred when check food.", e);
            response.setMessage("Error: Check food is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MainResponse getByName(String name, EFoodListStatus status) {
        MainResponse response = new MainResponse();
        FoodResponse result = new FoodResponse();

        Optional<Food> food;

        String url = Generator.generateUrl(name);

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.NOT_FOUND.ordinal());
        response.setMessage("Sonuç bulunamadı.");
        response.setResult(result);

        try {
            if (status == EFoodListStatus.ALL) food = foodRepo.findByUrlIgnoreCase(url);
            else food = foodRepo.findByUrlIgnoreCaseAndFoodStatus(url, EFoodStatus.ACCEPT);

            if (food.isPresent()) {
                List<FoodNutritional> foodNutritionalList = fNutrRepo.findAllByFood(food.get());
                Food cloneFood = food.get().copy();
                cloneFood.setFoodNutritional(foodNutritionalList);

                result.setFood(cloneFood);

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Sonuç(lar) bulundu.");
                response.setResult(result);
            }
        } catch (Exception e) {
            log.error("An error occurred when getting food by name.", e);
            response.setMessage("Error: Get food by name is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    private List<FoodNutritional> getFoodNutritionals(Food foods, Food food) {
        List<FoodNutritional> insertedFoodNutritionalList = new ArrayList<>();
        for (FoodNutritional fn : foods.getFoodNutritional()) {
            if (fn.getNutritional().getId() == null || fn.getRate() == null || fn.getPercent() == null) continue;

            Optional<Nutritional> nutritional = nutriRepo.findById(fn.getNutritional().getId());

            // TODO Nutritional cachelenmeli redisten alınmalı.
            if (nutritional.isPresent()) {
                FoodNutritional fnNew = new FoodNutritional();
                fnNew.setFood(food);
                fnNew.setNutritional(nutritional.get());
                fnNew.setRate(fn.getRate());
                fnNew.setPercent(fn.getPercent());
                fNutrRepo.save(fnNew);
                insertedFoodNutritionalList.add(fnNew);
            }
        }
        return insertedFoodNutritionalList;
    }
}