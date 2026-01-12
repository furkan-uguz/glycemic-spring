package com.glycemic.entity.repository.glycemic;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.glycemic.entity.model.glycemic.Nutritional;

public interface NutritionalRepository extends JpaRepository<Nutritional, Long> {

    Optional<Nutritional> findByName(String name);
}
