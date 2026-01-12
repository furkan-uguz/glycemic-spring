package com.glycemic.services.auth.model.validate.reset;

public record ValidateResetRequest(String forgetKey, String email) {
}
