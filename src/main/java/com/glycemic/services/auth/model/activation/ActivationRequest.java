package com.glycemic.services.auth.model.activation;

import java.io.Serial;
import java.io.Serializable;

public record ActivationRequest(String email, String activateKey) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1144332256337856437L;
}