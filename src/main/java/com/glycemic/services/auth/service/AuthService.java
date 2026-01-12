package com.glycemic.services.auth.service;

import java.util.*;

import com.glycemic.core.log.LoginLogManager;
import com.glycemic.core.mail.EmailSender;
import com.glycemic.core.model.MainResponse;
import com.glycemic.core.util.*;
import com.glycemic.services.auth.model.reset.ResetPasswordResponse;
import com.glycemic.services.auth.model.validate.ValidateRequest;
import com.glycemic.services.auth.model.validate.ValidateResponse;
import com.glycemic.services.auth.model.activation.ActivationResponse;
import com.glycemic.services.auth.model.register.RegisterRequest;
import com.glycemic.services.auth.model.validate.reset.ValidateResetRequest;
import com.glycemic.services.auth.model.validate.reset.ValidateResetResponse;
import com.glycemic.services.auth.util.ERoleTypes;
import com.glycemic.services.auth.util.EStatus;
import io.sentry.Sentry;
import jakarta.mail.MessagingException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.glycemic.core.jwt.JwtUtils;
import com.glycemic.entity.model.glycemic.City;
import com.glycemic.entity.model.glycemic.JwtSession;
import com.glycemic.entity.model.glycemic.Roles;
import com.glycemic.entity.model.glycemic.UserActivation;
import com.glycemic.entity.model.glycemic.UserResetPassword;
import com.glycemic.entity.model.glycemic.Users;
import com.glycemic.entity.repository.glycemic.CityRepository;
import com.glycemic.entity.repository.glycemic.JwtSessionRepository;
import com.glycemic.entity.repository.glycemic.RoleRepository;
import com.glycemic.entity.repository.glycemic.UserActivationRepository;
import com.glycemic.entity.repository.glycemic.UserRepository;
import com.glycemic.entity.repository.glycemic.UserResetPasswordRepository;
import com.glycemic.services.auth.model.activation.ActivationRequest;
import com.glycemic.services.auth.model.login.LoginRequest;
import com.glycemic.services.auth.model.reset.ResetPasswordRequest;
import com.glycemic.services.auth.model.login.LoginResponse;
import com.glycemic.core.security.UserDetailsImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import static com.glycemic.core.mail.EmailSender.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final JwtSessionRepository jwtRepo;
    private final RoleRepository roleRepo;
    private final CityRepository cityRepo;
    private final UserActivationRepository activationRepo;
    private final UserResetPasswordRepository resetPasswordRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder encoder;
    private final EmailSender mailSender;
    private final LoginLogManager loginLogManager;

    @Value("${app.activationExpire}")
    private Long activationExpireTime;

    @Value("${app.resetPassExpire}")
    private Long resetExpireTime;

    @Value("${app.siteUrl}")
    private String siteUrl;

    private static final String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
    private static final String PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

    @Transactional("transactionManager")
    public MainResponse login(LoginRequest request, String userAgent, String remoteAddr, String fingerPrint) {
        MainResponse response = new MainResponse();
        LoginResponse result = new LoginResponse();
        JwtSession jwtSession = new JwtSession();

        try {
            response.setStatus(false);
            response.setMessage("Error: Authentication is not reachable.");
            response.setErrors(HttpStatus.BAD_REQUEST);

            if (userAgent == null || remoteAddr == null || fingerPrint == null)
                response.setMessage("Error: Missing headers.");
            else if (!request.email().matches(EMAIL_REGEX))
                response.setMessage("Error: Email is invalid.");
            else if (!request.password().matches(PASSWORD_REGEX))
                response.setMessage("Error: Password must be at least 8 characters long and contain uppercase letters, numbers and special characters.");
            else if (Boolean.FALSE.equals(userRepo.existsByEmail(request.email()))) {
                response.setMessage("Error: Email not found!");
                response.setErrors(HttpStatus.UNAUTHORIZED);
            }
            else {
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtils.generateJwtToken(authentication, request.rememberMe());

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                Users user = new Users(userDetails.getId(), userDetails.getEmail(), userDetails.getPassword(), userDetails.getName(), userDetails.getSurname(), userDetails.getFullname(), userDetails.getEnable(), userDetails.getCity());

                List<JwtSession> sessionList = jwtRepo.findAllByUsers(user);
                if (!sessionList.isEmpty()) {
                    //User logged already.
                    //Detect before session, if match current user-agent then delete and return unauthentic
                    for (JwtSession session : sessionList) {
                        if (session.getFingerPrint() != null && session.getFingerPrint().equals(fingerPrint)) {
                            log.info("Double login attempt detected. User:{user:{}, session:{}}", request.email(), session.getId());
                            jwtRepo.delete(session);
                            response.setMessage("Error: Authentication already.");
                            response.setErrors(HttpStatus.UNAUTHORIZED);
                            return response;
                        }
                    }
                }

                jwtSession.setUsers(user);
                jwtSession.setJwttoken(jwt);
                jwtSession.setExpiretime(Boolean.TRUE.equals(request.rememberMe()) ? null : jwtUtils.getExpireTimeFromJwtToken(jwt));
                jwtSession.setRemoteAddr(remoteAddr);
                jwtSession.setUserAgent(userAgent);
                jwtSession.setFingerPrint(fingerPrint);
                jwtRepo.save(jwtSession);

                result.setId(userDetails.getId());
                result.setToken(jwt);
                result.setEmail(userDetails.getEmail());
                result.setFullname(userDetails.getFullname());
                result.setName(userDetails.getName());
                result.setSurname(userDetails.getSurname());
                result.setEnable(userDetails.getEnable());

                loginLogManager.loginLog(userDetails.getId());

                response.setStatus(true);
                response.setMessage("Login success");
                response.setErrors(HttpStatus.OK);
                response.setResult(result);
            }
        } catch (AuthenticationException e) {
            log.error("An AuthenticationException error occurred when logging.", e);
            response.setMessage("Error: Authentication failed.");
            response.setErrors(HttpStatus.UNAUTHORIZED);
        } catch(Exception e) {
            log.error("An error occurred when logging.", e);
            Sentry.captureException(e);

            response.setMessage("Error: Authentication is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Transactional("transactionManager")
    public MainResponse register(RegisterRequest request, String userAgent, String remoteAddr, String fingerPrint) {
        MainResponse response = new MainResponse();
        LoginResponse result = new LoginResponse();

        response.setStatus(false);
        response.setMessage("Error: Authentication is not reachable.");
        response.setErrors(HttpStatus.BAD_REQUEST);

        try {
            if (userAgent == null || remoteAddr == null || fingerPrint == null) {
                response.setMessage("Error: Missing headers.");
            } else if (!request.getEmail().matches(EMAIL_REGEX)) {
                response.setMessage("Error: Email is invalid.");
            } else if (!request.getPassword().matches(PASSWORD_REGEX)) {
                response.setMessage("Error: Password must be at least 8 characters long and contain uppercase letters, numbers and special characters.");
            } else if (Boolean.TRUE.equals(userRepo.existsByEmail(request.getEmail()))) {
                response.setMessage("Error: Password must be at least 8 characters long and contain uppercase letters, numbers and special characters.");
                response.setErrors(HttpStatus.UNAUTHORIZED);
            } else {
                // Create new user's account
                Users user = new Users(request.getEmail(), encoder.encode(request.getPassword()), request.getName(), request.getSurname(), false);

                List<Roles> roles = new ArrayList<>();
                Roles userRole = roleRepo.findByName(ERoleTypes.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

                City city = cityRepo.findById(request.getCity().getId()).orElseThrow(() -> new RuntimeException("Error: City is not found."));

                roles.add(userRole);
                user.setEnable(false);
                user.setRoles(roles);
                user.setCity(city);
                user.setFullname(user.getName() + " " + user.getSurname());
                user.setCreatedBy(user.getEmail());
                user.setModifiedBy(user.getEmail());
                user = userRepo.save(user);

                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtils.generateJwtToken(authentication, false);

                welcome(user.getEmail(), user.getName());
                jwtRepo.save(new JwtSession(user, jwt, jwtUtils.getExpireTimeFromJwtToken(jwt), remoteAddr, userAgent, fingerPrint));

                result.setId(user.getId());
                result.setToken(jwt);
                result.setEmail(user.getEmail());
                result.setFullname(user.getFullname());
                result.setName(user.getName());
                result.setSurname(user.getSurname());
                result.setEnable(user.getEnable());

                loginLogManager.loginLog(user.getId());
            }

            response.setStatus(true);
            response.setMessage("Registration with login is success");
            response.setErrors(HttpStatus.OK);
            response.setResult(result);
        } catch (AuthenticationException e) {
            log.error("An AuthenticationException error occurred when logging.", e);
            response.setMessage("Error: Authentication failed.");
            response.setErrors(HttpStatus.UNAUTHORIZED);
        } catch(Exception e) {
            log.error("An error occurred when logging.", e);
            Sentry.captureException(e);

            response.setMessage("Error: Authentication is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MainResponse activate(ActivationRequest request) {
        MainResponse response = new MainResponse();
        ActivationResponse result = new ActivationResponse();

        response.setStatus(false);
        response.setMessage("Error: Activation is invalid.");
        response.setErrors(HttpStatus.BAD_REQUEST);

        try {
            Optional<UserActivation> activationOpt = activationRepo.findByUserEmailAndUuid(request.email(), request.activateKey());

            if (activationOpt.isPresent()) {
                UserActivation activation = activationOpt.get();
                if (Boolean.TRUE.equals(activation.getActivated())) {
                    result.setResult(EStatus.ALREADY.ordinal());

                    response.setMessage("Error: Already activated.");
                    response.setErrors(HttpStatus.OK);
                    response.setResult(result);
                } else if (activation.getCreatedDate() + activationExpireTime < System.currentTimeMillis()) {
                    result.setResult(EStatus.EXPIRED.ordinal());

                    response.setMessage("Error: Activation is expired.");
                    response.setErrors(HttpStatus.OK);
                    response.setResult(result);
                } else {
                    activation.setActivated(true);
                    activationRepo.save(activation);

                    result.setResult(EStatus.OK.ordinal());

                    response.setStatus(true);
                    response.setMessage("Activation success.");
                    response.setErrors(HttpStatus.OK);
                    response.setResult(result);
                }
            }
        } catch(Exception e) {
            log.error("An error occurred when activate.", e);
            response.setMessage("Error: Activation is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public MainResponse validateReset(ValidateResetRequest request) {
        MainResponse response = new MainResponse();
        ValidateResetResponse result = new ValidateResetResponse();

        result.setResult(EStatus.INVALID.ordinal());

        response.setStatus(false);
        response.setMessage("Error: Parameters are invalid.");
        response.setErrors(HttpStatus.BAD_REQUEST);
        response.setResult(result);

        try {
            Optional<UserResetPassword> resetOpt = resetPasswordRepo.findByUserEmailAndUuid(request.email(), request.forgetKey());

            if (resetOpt.isPresent()) {
                UserResetPassword reset = resetOpt.get();
                if (Boolean.TRUE.equals(reset.getUsed())) {
                    result.setResult(EStatus.ALREADY.ordinal());

                    response.setMessage("Error: Link already used.");
                    response.setErrors(HttpStatus.OK);
                    response.setResult(result);
                } else if (reset.getCreatedDate() + resetExpireTime < System.currentTimeMillis()) {
                    result.setResult(EStatus.EXPIRED.ordinal());

                    response.setMessage("Error: Password reset is expired.");
                    response.setErrors(HttpStatus.OK);
                    response.setResult(result);
                } else {
                    result.setResult(EStatus.OK.ordinal());

                    response.setMessage("Link validated.");
                    response.setErrors(HttpStatus.OK);
                    response.setResult(result);
                }
            }
        } catch(Exception e) {
            log.error("An error occurred when reset validation.", e);
            Sentry.captureException(e);

            response.setMessage("Error: Validation is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @Transactional("transactionManager")
    public MainResponse resetPassword(ResetPasswordRequest request) {
        MainResponse response = new MainResponse();
        ResetPasswordResponse result = new ResetPasswordResponse();

        result.setResult(EStatus.INVALID.ordinal());

        response.setStatus(false);
        response.setMessage("Error: Parameters are invalid.");
        response.setErrors(HttpStatus.BAD_REQUEST);
        response.setResult(result);

       try {
           Optional<UserResetPassword> resetOpt = resetPasswordRepo.findByUserEmailAndUuid(request.email(), request.forgetKey());
           if (resetOpt.isPresent()) {
               if (!request.password().equals(request.passwordConfirm())) {
                   result.setResult(EStatus.EXPIRED.ordinal());

                   response.setErrors(HttpStatus.OK);
                   response.setResult(result);
               } else if (!request.password().matches(PASSWORD_REGEX)) {
                   result.setResult(EStatus.ALREADY.ordinal());

                   response.setMessage("Error: Password must be at least 8 characters long and contain uppercase letters, numbers and special characters.");
                   response.setErrors(HttpStatus.OK);
                   response.setResult(result);
               } else {
                   UserResetPassword reset = resetOpt.get();
                   Users user = reset.getUser();

                   user.setPassword(encoder.encode(request.password()));
                   reset.setUsed(true);

                   resetPasswordRepo.save(reset);
                   userRepo.save(user);

                   result.setResult(EStatus.OK.ordinal());

                   response.setStatus(true);
                   response.setMessage("Password changed successfully.");
                   response.setErrors(HttpStatus.OK);
                   response.setResult(result);
               }
           }
       } catch(Exception e) {
           log.error("An error occurred when reset password.", e);
           Sentry.captureException(e);

           response.setMessage("Error: Reset password is not reachable.");
           response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
       }
        return response;
    }

    public MainResponse validateUser(ValidateRequest request) {
        MainResponse response = new MainResponse();
        ValidateResponse result = new ValidateResponse();

        result.setResult(EStatus.INVALID.ordinal());

        response.setStatus(false);
        response.setMessage("Error: Parameters are invalid.");
        response.setErrors(HttpStatus.BAD_REQUEST);
        response.setResult(result);

        try {
            if (request.token().isEmpty() || request.token().isBlank() || request.email().isEmpty() || request.email().isBlank()) {
                result.setResult(EStatus.INVALID.ordinal());
                response.setResult(result);
            } else if (!jwtUtils.validateToken(request.token()) || !jwtUtils.getEmailFromJwtToken(request.token()).equals(request.email())) {
                result.setResult(EStatus.EXPIRED.ordinal());
                response.setResult(result);
            } else {
                Optional<JwtSession> jwtSessionOptional = jwtRepo.findByJwttoken(request.token());
                if (jwtSessionOptional.isEmpty() || !jwtSessionOptional.get().getUsers().getEmail().equals(request.email())) {
                    result.setResult(EStatus.EXPIRED.ordinal());
                    response.setResult(result);
                }
                else {
                    result.setResult(EStatus.EXPIRED.ordinal());

                    response.setStatus(true);
                    response.setMessage("Success");
                    response.setErrors(HttpStatus.OK);
                    response.setResult(result);
                }
            }
        } catch(Exception e) {
            log.error("An error occurred when user validation.", e);
            Sentry.captureException(e);

            response.setMessage("Error: Validation is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    public MainResponse validateEmail(ValidateRequest request) {
        MainResponse response = new MainResponse();
        ValidateResponse result = new ValidateResponse();

        result.setResult(EStatus.OK.ordinal());

        response.setStatus(true);
        response.setMessage("Success");
        response.setErrors(HttpStatus.OK);
        response.setResult(result);

        try {
            Optional<Users> user = userRepo.findByEmail(request.email());
            if (user.isPresent()) {
                result.setResult(EStatus.ALREADY.ordinal());

                response.setStatus(false);
                response.setMessage("User found with that email.");
                response.setResult(result);
            }
        } catch(Exception e) {
            log.error("An error occurred when email validation.", e);
            Sentry.captureException(e);

            response.setStatus(false);
            response.setMessage("Error: Validation is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResult(null);
        }

        return response;
    }

    public MainResponse logout(ValidateRequest request) {
        MainResponse response = new MainResponse();
        ValidateResponse result = new ValidateResponse();

        result.setResult(EStatus.OK.ordinal());

        response.setStatus(true);
        response.setMessage("Success");
        response.setErrors(HttpStatus.OK);
        response.setResult(result);

        try {
            Optional<JwtSession> session = jwtRepo.findByJwttoken(request.token());

            if (session.isPresent()) {
                jwtRepo.delete(session.get());
            } else
                response.setMessage("Token not found. Session is invalid but logout can applicable.");
        } catch(Exception e) {
            log.error("An error occurred when logout.", e);
            Sentry.captureException(e);

            response.setStatus(false);
            response.setMessage("Error: Logout is not reachable.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResult(null);
        }

        return response;
    }

    private String createActivateUrl(String email) {
        String uuid = Generator.generateUUID();
        String url = "";

        Optional<UserActivation> activationOpt = activationRepo.findByUserEmail(email);
        activationOpt.ifPresent(activationRepo::delete);

        Optional<Users> userOpt = userRepo.findByEmail(email);
        if (userOpt.isPresent()) {
            UserActivation activation = new UserActivation();
            activation.setUser(userOpt.get());
            activation.setUuid(uuid);
            activation.setActivated(false);
            activationRepo.save(activation);

            url = siteUrl + "activation?activateKey=" + uuid + "&to=" + email;
        }
        return url;
    }

    private void welcome(String to, String name) throws MessagingException {
        LinkedHashMap<String, Object> templateModel = new LinkedHashMap<>();
        String activateUrl = createActivateUrl(to);

        if (!activateUrl.isBlank()) {
            templateModel.put(RECIPIENT_NAME, name);
            templateModel.put(SENDER, "GlycemicApp");
            templateModel.put(ACTIVATE_URL, activateUrl);

            mailSender.sendSimpleMessageWithTemplate(to, "Hoş geldiniz", "mail-welcome.html", templateModel);
        }
    }
}
