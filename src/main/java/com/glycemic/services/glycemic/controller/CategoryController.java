package com.glycemic.services.glycemic.controller;

import com.glycemic.core.model.MainResponse;
import com.glycemic.entity.model.glycemic.Category;
import com.glycemic.services.glycemic.service.CategoryService;
import com.glycemic.services.glycemic.validator.category.CategoryAllValidator;
import com.glycemic.services.glycemic.validator.category.CategoryValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> getAllCategory() {
        MainResponse body = categoryService.categoryList();

        return new ResponseEntity<>(body, body.getErrors());
    }

    @GetMapping(path = "/foods", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> getAllCategoryWithFoods() {
        MainResponse body = categoryService.categoryListWithFoods();

        return new ResponseEntity<>(body, body.getErrors());
    }

    @GetMapping(path = "/find", params = {"name"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> getOneByNameUrl(@RequestParam("name") String name) {
        MainResponse body = categoryService.getCategoryBy(name, null);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @GetMapping(path = "/find/{id}", params = {"id"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> getOneById(@PathVariable("id") Long id) {
        MainResponse body = categoryService.getCategoryBy(null, id);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('SUPER_ADMIN')")
    @PutMapping(path = "/insert", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> insertCategory(@RequestBody @Validated(value = CategoryValidator.class) Category category) {
        MainResponse body = categoryService.insert(category);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('SUPER_ADMIN')")
    @PostMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> updateCategory(@RequestBody @Validated(value = CategoryAllValidator.class) Category category) {
        MainResponse body = categoryService.update(category);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('SUPER_ADMIN')")
    @DeleteMapping(path = "/delete", params = {"id"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> deleteCategory(@RequestParam("id") Long id) {
        MainResponse body = categoryService.delete(id);

        return new ResponseEntity<>(body, body.getErrors());
    }
}
