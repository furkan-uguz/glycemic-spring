package com.glycemic.entity.model.glycemic;

import java.io.Serial;
import java.io.Serializable;

import com.glycemic.entity.model.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.glycemic.services.glycemic.validator.nutritional.NutritionalIdValidator;
import com.glycemic.services.glycemic.validator.nutritional.NutritionalValidator;
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
public class Nutritional extends BaseModel implements Serializable {

    public Nutritional(Nutritional nutritional) {
        id = nutritional.id;
        name = nutritional.name;
        unit = nutritional.unit;
    }

    @Serial
    private static final long serialVersionUID = 1859918222450635883L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "Id kısmı 1 den küçük olamaz.", groups = NutritionalIdValidator.class)
    private Long id;

    @NotNull(message = "İsim kısmı boş bırakılamaz", groups = NutritionalValidator.class)
    private String name;

    @NotNull(message = "Birim kısmı boş bırakılamaz", groups = NutritionalValidator.class)
    private String unit;
}
