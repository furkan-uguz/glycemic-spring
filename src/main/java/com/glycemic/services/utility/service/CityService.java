package com.glycemic.services.utility.service;

import com.glycemic.core.model.MainResponse;
import com.glycemic.core.util.CacheNames;
import com.glycemic.entity.model.glycemic.City;
import com.glycemic.entity.repository.glycemic.CityRepository;
import com.glycemic.services.utility.model.city.CityListResponse;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepo;

    @Cacheable(CacheNames.CITY_CACHE)
    public MainResponse cityList() {
        MainResponse response = new MainResponse();
        CityListResponse result = new CityListResponse();

        response.setStatus(false);
        response.setErrors(HttpStatus.OK);
        response.setMessage("Şehir(ler) bulunamadı.");
        response.setResult(result);

        try {
            List<City> cityList = cityRepo.findAll();

            if (!cityList.isEmpty()) {
                result.setCityList(cityList);

                response.setStatus(true);
                response.setMessage("Şehir(ler) bulundu.");
                response.setResult(result);
            }
        } catch (Exception e) {
            log.error("An error occurred when getting city list.", e);
            Sentry.captureException(e);
            response.setMessage("Error: Cities is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MainResponse insertCity(City city) {
        MainResponse response = new MainResponse();
        log.error("TEST ERROR");
        Sentry.logger().info("City Insert Executed.");
        response.setErrors(HttpStatus.OK);
        return response;
    }
}
