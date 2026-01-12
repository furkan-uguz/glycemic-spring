package com.glycemic.core.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.glycemic.entity.model.glycemic.City;
import com.glycemic.entity.model.glycemic.Users;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private final Long id;

    @Getter
    private final String fullname;

    @Getter
    private final String name;

    @Getter
    private final String surname;

    @Getter
    private final String email;

    @Getter
    private final City city;

    @Getter
    private final Boolean enable;

    @Getter
    private final String createdBy;

    @Getter
    private final Long createdDate;

    @Getter
    private final String modifiedBy;

    @Getter
    private final Long modifiedDate;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String email, String password, String name, String surname, String fullname, Boolean enable, City city, String createdBy, Long createdDate, String modifiedBy, Long modifiedDate, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.fullname = fullname;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.city = city;
        this.enable = enable;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(Users user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

        return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword(), user.getName(), user.getSurname(), user.getFullname(), user.getEnable(), user.getCity(), user.getCreatedBy(), user.getCreatedDate(), user.getModifiedBy(), user.getModifiedDate(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}