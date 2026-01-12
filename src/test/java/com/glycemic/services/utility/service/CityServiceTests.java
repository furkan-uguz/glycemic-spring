package com.glycemic.services.utility.service;

import com.glycemic.services.utility.model.city.CityListResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CityServiceTests {

    @Autowired
    CityService cityService;

    @Test
    void get_city_list_example() {
        CityListResponse response = (CityListResponse) cityService.cityList().getResult();

        assertThat(response.getCityList()).hasSizeGreaterThan(0);
    }
}
