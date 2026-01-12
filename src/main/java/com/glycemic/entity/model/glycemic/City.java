package com.glycemic.entity.model.glycemic;

import java.io.Serial;
import java.io.Serializable;

import com.glycemic.entity.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.glycemic.services.utility.serializer.CitySerializer;
import com.glycemic.services.utility.validator.CountryIdValidator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(schema = "glycemic")
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = CitySerializer.class)
public class City extends BaseModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 7727106370962024058L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "Id kısmı 1 den küçük olamaz.", groups = CountryIdValidator.class)
    private Long id;

    @NotNull(message = "İsim kısmı boş bırakılamaz.")
    @Column(unique = true)
    private String name;

    @NotNull(message = "Değer kısmı boş bırakılamaz.")
    private String value;
}
