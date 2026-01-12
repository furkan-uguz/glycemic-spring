package com.glycemic.services.auth.model.login;

import java.io.Serial;
import java.io.Serializable;

public record LoginRequest(String email, String password, Boolean rememberMe) implements Serializable {

    @Serial
    private static final long serialVersionUID = 7256070083621818341L;
}
