package com.glycemic.services.glycemic.controller;

import com.glycemic.core.model.MainResponse;
import com.glycemic.entity.model.glycemic.Nutritional;
import com.glycemic.services.glycemic.service.NutritionalService;
import com.glycemic.services.glycemic.validator.nutritional.NutritionalAllValidator;
import com.glycemic.services.glycemic.validator.nutritional.NutritionalValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/nutritional")
public class NutritionalController {

    private final NutritionalService nutritionalService;

    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> list() {
        MainResponse body = nutritionalService.nutritionalList();

        return new ResponseEntity<>(body, body.getErrors());
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('SUPER_ADMIN')")
    @PutMapping(path = "/insert", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> insertCategory(@RequestBody @Validated(value = NutritionalValidator.class) Nutritional category) {
        MainResponse body = nutritionalService.insert(category);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('SUPER_ADMIN')")
    @PostMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> updateCategory(@RequestBody @Validated(value = NutritionalAllValidator.class) Nutritional category) {
        MainResponse body = nutritionalService.update(category);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @PreAuthorize("hasRole('ADMIN') OR hasRole('SUPER_ADMIN')")
    @DeleteMapping(path = "/delete", params = {"id"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> deleteCategory(@RequestParam("id") Long id) {
        MainResponse body = nutritionalService.delete(id);

        return new ResponseEntity<>(body, body.getErrors());
    }
}
