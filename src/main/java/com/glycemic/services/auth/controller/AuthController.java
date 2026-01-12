package com.glycemic.services.auth.controller;

import com.glycemic.core.model.MainResponse;
import com.glycemic.services.auth.model.activation.ActivationRequest;
import com.glycemic.services.auth.model.login.LoginRequest;
import com.glycemic.services.auth.model.register.RegisterRequest;
import com.glycemic.services.auth.model.reset.ResetPasswordRequest;
import com.glycemic.services.auth.model.validate.ValidateRequest;
import com.glycemic.services.auth.model.validate.reset.ValidateResetRequest;
import com.glycemic.services.auth.service.AuthService;
import com.glycemic.services.auth.validator.user.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        String userAgent = httpServletRequest.getHeader("User-Agent");
        String fingerPrint = httpServletRequest.getHeader("Fingerprint");
        String remoteAddress = httpServletRequest.getRemoteAddr();

        MainResponse body = authService.login(request, userAgent, remoteAddress, fingerPrint);
        return new ResponseEntity<>(body, body.getErrors());
    }

    @PostMapping(path = "/signup", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> register(@RequestBody @Validated(value = UserValidator.class) RegisterRequest request, HttpServletRequest httpServletRequest) {
        String userAgent = httpServletRequest.getHeader("User-Agent");
        String fingerPrint = httpServletRequest.getHeader("Fingerprint");
        String remoteAddress = httpServletRequest.getRemoteAddr();

        MainResponse body = authService.register(request, userAgent, remoteAddress, fingerPrint);
        return new ResponseEntity<>(body, body.getErrors());
    }

    @PostMapping(path = "/activate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> activateUser(@RequestBody ActivationRequest request) {
        MainResponse body = authService.activate(request);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @GetMapping(path = "/validate_reset", params = {"forgetKey", "email"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> validateReset(@RequestBody ValidateResetRequest request) {
        MainResponse body = authService.validateReset(request);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @PostMapping(path = "/reset_password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        MainResponse body = authService.resetPassword(request);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @PostMapping(path = "/validate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> validateUser(@RequestBody ValidateRequest request) {
        MainResponse body = authService.validateUser(request);

        return new ResponseEntity<>(body, body.getErrors());
    }

    @PostMapping(path = "/validate_email", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> validateEmail(@RequestBody ValidateRequest request) {
        MainResponse body = authService.validateEmail(request);

        return new ResponseEntity<>(body, body.getErrors());
    }


    @PostMapping(path = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> logout(@RequestBody ValidateRequest request) {
        MainResponse body = authService.logout(request);

        return new ResponseEntity<>(body, body.getErrors());
    }
}
