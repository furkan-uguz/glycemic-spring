package com.glycemic.entity.repository.glycemic;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glycemic.entity.model.glycemic.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByUrlEqualsIgnoreCase(String url);

    Optional<Category> findByNameEqualsIgnoreCase(String name);
}
