package com.glycemic.entity.model.glycemic;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import com.glycemic.entity.model.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glycemic.services.glycemic.serializer.CategorySerializer;
import com.glycemic.services.glycemic.validator.category.CategoryIdValidator;
import com.glycemic.services.glycemic.validator.category.CategoryValidator;
import com.glycemic.services.glycemic.view.NutritionalView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(schema = "glycemic")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonView(NutritionalView.ExceptFood.class)
public class Category extends BaseModel implements Serializable {

    public Category(Category category) {
        id = category.id;
        name = category.name;
        url = category.url;
        foods = category.foods;
    }

    @Serial
    private static final long serialVersionUID = 8290255678413836321L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "Id kısmı 1 den küçük olamaz.", groups = {CategoryIdValidator.class})
    private Long id;

    @NotNull(message = "İsim kısmı boş bırakılamaz.", groups = CategoryValidator.class)
    @JoinColumn(unique = true)
    private String name;

    private String url;

    @JsonSerialize(using = CategorySerializer.class)
    @Transient
    private List<Food> foods;

    public Category(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category [id=" + id + ", name=" + name + ", url=" + url + "]";
    }
}
