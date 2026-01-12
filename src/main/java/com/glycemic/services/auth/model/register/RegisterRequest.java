package com.glycemic.services.auth.model.register;

import com.glycemic.entity.model.glycemic.City;
import com.glycemic.services.auth.validator.user.UserValidator;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -1915197722847011881L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Email kısmı boş bırakılamaz.", groups = UserValidator.class)
    @Size(max = 50, message = "Email en fazla 50 karakter içerebilir.", groups = UserValidator.class)
    @Column(unique = true, length = 50)
    private String email;

    @NotNull(message = "Şifre kısmı boş bırakılamaz.", groups = UserValidator.class)
    private String password;

    @NotNull(message = "İsim kısmı boş bırakılamaz.", groups = UserValidator.class)
    private String name;

    @NotNull(message = "Soyisim kısmı boş bırakılamaz.", groups = UserValidator.class)
    private String surname;

    private String fullname;

    private Boolean enable;

    @NotNull(message = "Şehir kısmı boş bırakılamaz.", groups = UserValidator.class)
    private City city;
}
