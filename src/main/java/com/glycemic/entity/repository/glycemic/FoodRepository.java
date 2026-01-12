package com.glycemic.entity.repository.glycemic;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.glycemic.entity.model.glycemic.Category;
import com.glycemic.entity.model.glycemic.Food;
import com.glycemic.services.glycemic.util.EFoodStatus;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    Optional<Food> findByNameEqualsIgnoreCase(String name);

    Optional<Food> findByUrlIgnoreCase(String url);

    Optional<Food> findByUrlIgnoreCaseAndFoodStatus(String url, EFoodStatus foodStatus);

    List<Food> findByCreatedByEqualsIgnoreCase(String createdBy);

    @Query(value = "SELECT * FROM food WHERE CREATED_BY = :created_by ORDER BY name ASC", countQuery = "SELECT COUNT(*) FROM food WHERE created_by = :created_by", nativeQuery = true)
    Page<Food> findByCreatedByWithPage(@Param("created_by") String createdBy, Pageable pageable);

    Optional<Food> findByCreatedByEqualsIgnoreCaseAndIdEquals(String createdBy, Long id);

    List<Food> findAllByCategory(Category category);

    @Query(value = "SELECT * FROM food WHERE category_id = :category_id AND food_status = :food_status ORDER BY name ASC", countQuery = "SELECT COUNT(*) FROM food WHERE category_id = :category_id AND food_status = :food_status", nativeQuery = true)
    Page<Food> findByCategoryIdPage(@Param("category_id") Long categoryId, @Param("food_status") Integer foodStatus, Pageable pageable);

    @Query(value = "SELECT * FROM food WHERE FOOD_STATUS = :food_status ORDER BY NAME ASC", countQuery = "SELECT COUNT(*) FROM FOOD WHERE FOOD_STATUS = :food_status", nativeQuery = true)
    Page<Food> findAllPageable(@Param("food_status") Integer foodStatus, Pageable pageable);

    @Query(value = "SELECT f.* FROM food f INNER JOIN category c ON f.category_id = c.id WHERE LOWER(f.name) LIKE LOWER(CONCAT('%',:name,'%')) AND c.url LIKE :category AND f.food_status = :food_status ORDER BY f.name", nativeQuery = true, countQuery = "SELECT COUNT(f.id) FROM food f INNER JOIN category c ON f.category_id = c.id WHERE LOWER(f.name) LIKE LOWER(CONCAT('%',:name,'%')) AND c.url LIKE :category AND f.food_status = :food_status")
    Page<Food> foodsNameWithCategoryJoinAndLimited(@Param("name") String name, @Param("category") String category, @Param("food_status") Integer foodStatus, Pageable pageable);

    @Query(value = "SELECT f.* FROM food f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%',:name,'%')) AND f.food_status = :food_status ORDER BY f.name", nativeQuery = true, countQuery = "SELECT COUNT(f.id) FROM food f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%',:name,'%')) AND f.food_status = :food_status")
    Page<Food> foodsNameWithAll(@Param("name") String name, @Param("food_status") Integer foodStatus, Pageable pageable);

    @Query(value = "SELECT f.* FROM food f INNER JOIN category c ON f.category_id = c.id WHERE LOWER(f.name) LIKE LOWER(CONCAT('%',:name,'%')) AND c.url LIKE :category AND f.create_by = :created_by ORDER BY f.name", nativeQuery = true, countQuery = "SELECT COUNT(f.id) FROM food f INNER JOIN category c ON f.category_id = c.id WHERE LOWER(f.name) LIKE LOWER(CONCAT('%',:name,'%')) AND c.url LIKE :category AND f.created_by = :created_by")
    Page<Food> foodsNameWithCategoryJoinAndLimitedForUser(@Param("name") String name, @Param("category") String category, @Param("created_by") String createdBy, Pageable pageable);

    @Query(value = "SELECT f.* FROM food f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%',:name,'%')) AND f.created_by = :created_by ORDER BY f.name", nativeQuery = true, countQuery = "SELECT COUNT(f.id) FROM food f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%',:name,'%')) AND f.created_by = :created_by")
    Page<Food> foodsNameWithAllForUser(@Param("name") String name, @Param("created_by") String createdBy, Pageable pageable);
}
