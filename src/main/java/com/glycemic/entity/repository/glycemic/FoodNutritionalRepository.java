package com.glycemic.entity.repository.glycemic;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glycemic.entity.model.glycemic.Food;
import com.glycemic.entity.model.glycemic.FoodNutritional;

@Repository
public interface FoodNutritionalRepository extends JpaRepository<FoodNutritional, Long> {
    List<FoodNutritional> findAllByFood(Food food);

    List<FoodNutritional> findAllByFoodId(Integer foodId);
}
