package com.glycemic.services.glycemic.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.glycemic.core.model.MainResponse;
import com.glycemic.entity.model.glycemic.Food;
import com.glycemic.services.glycemic.service.FoodService;
import com.glycemic.services.glycemic.util.EFoodListStatus;
import com.glycemic.services.glycemic.validator.food.FoodAllValidator;
import com.glycemic.services.glycemic.validator.food.FoodValidator;
import com.glycemic.services.glycemic.view.NutritionalView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/food")
public class FoodController {

    private final FoodService foodService;

    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> list() {
        MainResponse body = foodService.foodList();

        return new ResponseEntity<>(body, body.getErrors());
    }

    @GetMapping(path = "/list/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> userFoodList() {
        MainResponse body = foodService.userFoodList();

        return new ResponseEntity<>(body, body.getErrors());
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('SUPER_ADMIN')")
    @DeleteMapping(path = "/delete/{id}", params = {"id"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> foodDelete(@PathVariable("id") Long id) {
        MainResponse body = foodService.delete(id);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @PutMapping(path = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> foodInsert(@RequestBody @Validated(value = FoodValidator.class) Food food) {
        MainResponse body = foodService.insert(food);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @PostMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> foodUpdate(@RequestBody @Validated(value = FoodAllValidator.class) Food food) {
        MainResponse body = foodService.update(food);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @GetMapping(path = "/check/{id}", params = {"id"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> foodCheck(@PathVariable("id") Long id) {
        MainResponse body = foodService.check(id);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @JsonView(NutritionalView.ExceptFood.class)
    @GetMapping(path = "/get", params = {"name"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> foodGetByName(@RequestParam("name") String name) {
        MainResponse body = foodService.getByName(name, EFoodListStatus.REJECT);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN') OR hasRole('SUPER_ADMIN')")
    @JsonView(NutritionalView.ExceptFood.class)
    @GetMapping(path = "/get", params = {"name", "status"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> foodGetByNameAndStatus(@RequestParam("name") String name, @RequestParam("status") EFoodListStatus status) {
        MainResponse body = foodService.getByName(name, status);

        return new ResponseEntity<>(body, body.getErrors());
    }
}
