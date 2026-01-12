package com.glycemic.entity.repository.glycemic;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glycemic.entity.model.glycemic.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findCityByName(String name);

    Optional<City> findCityByValue(String value);

}
