package com.glycemic.entity.model.glycemic;

import java.io.Serial;
import java.io.Serializable;

import com.glycemic.entity.model.BaseModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.glycemic.services.glycemic.validator.foodnutritional.FoodNutritionalIdValidator;
import com.glycemic.services.glycemic.validator.foodnutritional.FoodNutritionalValidator;
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
public class FoodNutritional extends BaseModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 7939291708698036552L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "Id kısmı 1 den küçük olamaz.", groups = FoodNutritionalIdValidator.class)
    @JsonView(NutritionalView.ExceptFood.class)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @NotNull(message = "Yiyecek alanı boş bırakılmaz.", groups = FoodNutritionalValidator.class)
    private Food food;

    @ManyToOne(cascade = CascadeType.DETACH)
    @NotNull(message = "Besin değeri türü boş bırakılmaz.", groups = FoodNutritionalValidator.class)
    @JsonView(NutritionalView.ExceptFood.class)
    private Nutritional nutritional;

    @NotNull(message = "Besin değerleri alanı boş bırakılmaz.", groups = FoodNutritionalValidator.class)
    @JsonView(NutritionalView.ExceptFood.class)
    private Integer rate;

    @NotNull(message = "Besin değerleri yüzdelik alanı boş bırakılmaz.", groups = FoodNutritionalValidator.class)
    @JsonView(NutritionalView.ExceptFood.class)
    private Integer percent;
}
