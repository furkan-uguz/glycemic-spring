package com.glycemic.services.utility.controller;

import com.glycemic.core.model.MainResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CityControllerTests {

    @Autowired
    CityController cityController;

    @Test
    void get_city_list_service_response() {
        ResponseEntity<MainResponse> body = cityController.getAllCity();

        assertThat(body.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
    }

}
