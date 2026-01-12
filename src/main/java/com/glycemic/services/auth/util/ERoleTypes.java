package com.glycemic.services.auth.util;

import lombok.Getter;

@Getter
public enum ERoleTypes {
	ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_SUPERADMIN("ROLE_SUPERADMIN");

    private final String value;

    ERoleTypes(String value) {
        this.value = value;
    }
}
