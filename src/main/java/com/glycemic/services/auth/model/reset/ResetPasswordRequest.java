package com.glycemic.services.auth.model.reset;

public record ResetPasswordRequest(String email, String forgetKey, String password, String passwordConfirm) {
}
