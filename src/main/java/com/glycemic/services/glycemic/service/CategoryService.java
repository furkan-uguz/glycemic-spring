package com.glycemic.services.glycemic.service;

import java.util.*;

import com.glycemic.core.model.MainResponse;
import com.glycemic.services.glycemic.model.category.list.CategoryListResponse;
import com.glycemic.services.glycemic.model.category.from.CategoryByResponse;
import com.glycemic.services.glycemic.model.category.cud.CategoryCUDResponse;
import com.glycemic.services.glycemic.util.EGlycemicErrorTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.glycemic.entity.model.glycemic.Category;
import com.glycemic.entity.model.glycemic.Food;
import com.glycemic.entity.repository.glycemic.CategoryRepository;
import com.glycemic.entity.repository.glycemic.FoodRepository;
import com.glycemic.services.glycemic.util.EFoodStatus;
import com.glycemic.core.util.Generator;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository cateRepo;
    private final FoodRepository foodRepo;

    @Value("${app.pageElementSize}")
    private Integer pageSize;

    public MainResponse categoryList() {
        MainResponse response = new MainResponse();
        CategoryListResponse result = new CategoryListResponse();

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.NOT_FOUND.ordinal());
        response.setMessage("Kategori(ler) bulunamadı.");
        response.setResult(result);

        try {
            //TODO REDİS KUR VE CACHE YAP
            List<Category> categoryList = cateRepo.findAll();

            if (!categoryList.isEmpty()) {
                result.setCategoryList(categoryList);

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Kategori(ler) bulundu.");
            }
        } catch (Exception e) {
            log.error("An error occurred when getting category list.", e);
            response.setMessage("Error: Category list is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MainResponse categoryListWithFoods() {
        MainResponse response = new MainResponse();
        CategoryListResponse result = new CategoryListResponse();

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.NOT_FOUND.ordinal());
        response.setMessage("Kategori(ler) bulunamadı.");
        response.setResult(result);

        try {
            //TODO REDİS KUR VE CACHE YAP
            List<Category> categories = cateRepo.findAll();

            if (!categories.isEmpty()) {
                for (Category category : categories) {
                    List<Food> foods = foodRepo.findAllByCategory(category);
                    category.setFoods(foods);
                }

                result.setCategoryList(categories);

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Kategori(ler) bulundu.");
                response.setResult(result);
            }
        } catch (Exception e) {
            log.error("An error occurred when getting category list with foods.", e);
            response.setMessage("Error: Category list with foods is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MainResponse getCategoryBy(String name, Long id) {
        MainResponse response = new MainResponse();
        CategoryByResponse result = new CategoryByResponse();
        Optional<Category> category;

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.NOT_FOUND.ordinal());
        response.setMessage("Kategori bulunamadı.");
        response.setResult(result);

        try {
            if (id == null) {
                String url = Generator.generateUrl(name);
                category = cateRepo.findByUrlEqualsIgnoreCase(url);
            } else category = cateRepo.findById(id);

            if (category.isPresent()) {
                Pageable pageable = PageRequest.of(0, pageSize);
                Page<Food> foods = foodRepo.findByCategoryIdPage(category.get().getId(), EFoodStatus.ACCEPT.ordinal(), pageable);
                category.get().setFoods(foods.getContent());

                result.setCategory(category.get());
                result.setPage(1);
                result.setTotalElements(foods.getTotalElements());
                result.setTotalPage(foods.getTotalPages());

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Kategori bulundu.");
                response.setResult(result);
            }
        } catch (Exception e) {
            log.error("An error occurred when getting category by option.", e);
            response.setMessage("Error: Category by option is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MainResponse insert(Category category) {
        MainResponse response = new MainResponse();
        CategoryCUDResponse result = new CategoryCUDResponse();

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setErrorCode(EGlycemicErrorTypes.INSERTED_BEFORE.ordinal());
        response.setMessage("Kategori daha önce eklenmiş.");
        response.setResult(result);

        try {
            Optional<Category> optCategory = cateRepo.findByNameEqualsIgnoreCase(category.getName());

            if (optCategory.isEmpty()) {
                String url = Generator.generateUrl(category.getName());
                category.setUrl(url);
                Category saved = cateRepo.save(category);

                result.setCategory(saved);

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Kategori başarıyla eklendi.");
                response.setResult(result);
            }
        } catch (Exception e) {
            log.error("An error occurred when insert new category.", e);
            response.setMessage("Error: Insert category is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MainResponse delete(Long id) {
        MainResponse response = new MainResponse();
        CategoryCUDResponse result = new CategoryCUDResponse();

        response.setStatus(false);
        response.setErrorCode(EGlycemicErrorTypes.NOT_FOUND.ordinal());
        response.setErrors(HttpStatus.OK);
        response.setMessage("Kategori bulunamadı.");
        response.setResult(result);

        Optional<Category> category = cateRepo.findById(id);

        try {
            if (category.isPresent()) {
                Category deleted = new Category(category.get());
                cateRepo.delete(category.get());

                result.setCategory(deleted);

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Kategori başarıyla temizlendi.");
                response.setResult(result);
            }
        } catch (Exception e) {
            log.error("An error occurred when delete category.", e);
            response.setErrorCode(EGlycemicErrorTypes.ERROR.ordinal());
            response.setMessage("Kategoriyi silerken hata ile karşılaşıldı. İşlem gerçekleştirilemedi.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MainResponse update(Category category) {
        MainResponse response = new MainResponse();
        CategoryCUDResponse result = new CategoryCUDResponse();

        response.setStatus(false);
        response.setErrorCode(EGlycemicErrorTypes.NOT_FOUND.ordinal());
        response.setErrors(HttpStatus.OK);
        response.setMessage("Kategori bulunamadı.");
        response.setResult(result);

        try {
            Optional<Category> searched = cateRepo.findById(category.getId());

            if (searched.isPresent()) {
                String url = Generator.generateUrl(category.getName());

                category.setUrl(url);
                category.setCreatedBy(searched.get().getCreatedBy());
                category.setCreatedDate(searched.get().getCreatedDate());

                Category updated = cateRepo.saveAndFlush(category);

                result.setCategory(updated);

                response.setStatus(true);
                response.setErrorCode(EGlycemicErrorTypes.OK.ordinal());
                response.setMessage("Kategori başarıyla güncellendi.");
                response.setResult(result);
            }
        } catch (Exception e) {
            log.error("An error occurred when update category.", e);
            response.setMessage("Error: Update category is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }
}
