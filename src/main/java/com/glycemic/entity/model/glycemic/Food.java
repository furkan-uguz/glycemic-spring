package com.glycemic.entity.model.glycemic;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import com.glycemic.entity.model.BaseModel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.glycemic.services.glycemic.util.EFoodStatus;
import com.glycemic.services.glycemic.validator.food.FoodIdValidator;
import com.glycemic.services.glycemic.validator.food.FoodValidator;
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
public class Food extends BaseModel implements Serializable, Cloneable {

    @Serial
    private static final long serialVersionUID = 1821243022594572233L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "Id kısmı 1 den küçük olamaz.", groups = FoodIdValidator.class)
    private Long id;

    @NotNull(message = "İsim kısmı boş bırakılamaz.", groups = FoodValidator.class)
    @Column(unique = true)
    private String name;

    @NotNull(message = "Glisemik indeks kısmı boş bırakılamaz.", groups = FoodValidator.class)
    private Integer glycemicIndex;

    @NotNull(message = "İnsülin kısmı boş bırakılamaz.", groups = FoodValidator.class)
    private Integer insulin;

    @NotNull(message = "Kalori kısmı boş bırakılamaz.", groups = FoodValidator.class)
    private Integer calori;

    @NotNull(message = "Karbonhidrat kısmı boş bırakılamaz.", groups = FoodValidator.class)
    private Float carbs;

    @NotNull(message = "Porsiyon kısmı boş bırakılamaz.", groups = FoodValidator.class)
    private Float serving;

    @NotNull(message = "Asitlik kısmı boş bırakılamaz.", groups = FoodValidator.class)
    private Float acidity;

    @NotNull(message = "Resim kısmı boş bırakılamaz.", groups = FoodValidator.class)
    private String image;

    @NotNull(message = "Kaynak kısmı boş bırakılamaz.", groups = FoodValidator.class)
    private String source;

    private String url;

    private boolean enabled;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @NotNull(message = "Kategori kısmı boş bırakılamaz.", groups = FoodValidator.class)
    @JoinColumn(name = "category_id")
    private Category category;

    private EFoodStatus foodStatus;

    @Transient
    private List<FoodNutritional> foodNutritional;

    public Food copy() {
        try {
            return (Food) this.clone();
        } catch (CloneNotSupportedException _) {
            return null;
        }
    }
}
