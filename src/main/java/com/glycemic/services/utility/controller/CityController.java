package com.glycemic.services.utility.controller;

import com.glycemic.core.model.MainResponse;
import com.glycemic.entity.model.glycemic.City;
import com.glycemic.services.utility.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/util/city")
public class CityController {

    private final CityService cityService;

    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> getAllCity() {
        MainResponse body = cityService.cityList();

        return new ResponseEntity<>(body, body.getErrors());
    }

    @PutMapping(path = "/insert", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> insertCity(City city) {
        MainResponse body = cityService.insertCity(city);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @DeleteMapping(path = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> deleteCity(@PathVariable("id") String id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
