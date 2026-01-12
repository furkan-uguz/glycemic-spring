package com.glycemic.entity.model.glycemic;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(schema = "glycemic")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtSession implements Serializable {

    @Serial
    private static final long serialVersionUID = 8937958151681015389L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @NotNull
    private Users users;

    @NotNull
    private String jwttoken;

    private Long expiretime;

    private String remoteAddr;

    private String userAgent;

    private String fingerPrint;

    public JwtSession(Users users, String jwttoken, Long expiretime, String remoteAddr, String userAgent, String fingerPrint) {
        this.users = users;
        this.jwttoken = jwttoken;
        this.expiretime = expiretime;
        this.remoteAddr = remoteAddr;
        this.userAgent = userAgent;
        this.fingerPrint = fingerPrint;
    }

    @Override
    public String toString() {
        return "";
    }
}
