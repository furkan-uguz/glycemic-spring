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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.glycemic.services.auth.validator.user.UserValidator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Table(name = "users", schema = "glycemic")
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Users extends BaseModel implements Serializable {

    @Serial
    private static final long serialVersionUID = -1915197722847011881L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Email kısmı boş bırakılamaz.", groups = UserValidator.class)
    @Size(max = 50, message = "Email en fazla 50 karakter içerebilir.", groups = UserValidator.class)
    @Column(unique = true, length = 50)
    private String email;

    @NotNull(message = "şifre kısmı boş bırakılamaz.", groups = UserValidator.class)
    private String password;

    @NotNull(message = "İsim kısmı boş bırakılamaz.", groups = UserValidator.class)
    private String name;

    @NotNull(message = "Soyisim kısmı boş bırakılamaz.", groups = UserValidator.class)
    private String surname;

    private String fullname;

    private Boolean enable;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @NotNull(message = "Şehir kısmı boş bırakılamaz.", groups = UserValidator.class)
    private City city;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Roles> roles;

    public Users(Long id, String email, String password, String name, String surname, String fullname, Boolean enable, City city) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.fullname = fullname;
        this.enable = enable;
        this.city = city;
    }

    public Users(String email, String password, String name, String surname, Boolean enable) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.enable = enable;
    }
}
