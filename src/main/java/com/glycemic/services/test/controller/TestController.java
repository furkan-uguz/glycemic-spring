package com.glycemic.services.test.controller;

import com.glycemic.core.model.MainResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("local")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MainResponse> testGet() {
        MainResponse body = new MainResponse();

        return new ResponseEntity<>(body, body.getErrors());
    }
}
