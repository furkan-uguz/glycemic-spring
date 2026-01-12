package com.glycemic.services.mail.service;

import java.util.LinkedHashMap;
import java.util.Optional;

import com.glycemic.core.model.MainResponse;
import com.glycemic.services.mail.model.forget.ForgetMailRequest;
import com.glycemic.services.mail.model.forget.ForgetMailResponse;
import jakarta.mail.MessagingException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.glycemic.core.mail.EmailSender;
import com.glycemic.entity.model.glycemic.UserResetPassword;
import com.glycemic.entity.model.glycemic.Users;
import com.glycemic.entity.repository.glycemic.UserRepository;
import com.glycemic.entity.repository.glycemic.UserResetPasswordRepository;
import com.glycemic.services.mail.util.EMailStatus;
import com.glycemic.core.util.Generator;
import org.springframework.transaction.annotation.Transactional;

import static com.glycemic.core.mail.EmailSender.FORGET_PASS_URL;
import static com.glycemic.core.mail.EmailSender.SENDER;

@Service
@RequiredArgsConstructor
public class MailService {

    private final EmailSender mailSender;

    private final UserResetPasswordRepository resetPasswordRepo;

    private final UserRepository userRepo;

    @Value("${app.siteUrl}")
    private String siteUrl;

    @Transactional("transactionManager")
    public MainResponse forgetPassword(ForgetMailRequest request) {
        MainResponse response = new MainResponse();
        ForgetMailResponse result = new ForgetMailResponse();

        LinkedHashMap<String, Object> templateModel = new LinkedHashMap<>();

        result.setResult(EMailStatus.ERROR.ordinal());

        response.setStatus(false);
        response.setMessage("Error: Some mistakes");
        response.setErrors(HttpStatus.BAD_REQUEST);
        response.setResult(result);

        try {
            Optional<Users> user = userRepo.findByEmail(request.email());

            if (user.isEmpty()) {
                result.setResult(EMailStatus.INVALID.ordinal());
                response.setMessage("Error: Mail address is invalid.");
                response.setErrors(HttpStatus.OK);
                response.setResult(result);
            } else {
                String forgetPasswordUrl = createForgetPasswordUrl(request.email());

                if (!forgetPasswordUrl.isBlank()) {
                    templateModel.put(SENDER, "GlycemicApp");
                    templateModel.put(FORGET_PASS_URL, forgetPasswordUrl);

                    mailSender.sendSimpleMessageWithTemplate(request.email(), "Şifreni sıfırla", "mail-forgetPassword.html", templateModel);

                    result.setResult(EMailStatus.ERROR.ordinal());

                    response.setStatus(true);
                    response.setMessage("Message sent.");
                    response.setErrors(HttpStatus.OK);
                    response.setResult(result);
                }
            }
        }  catch (MessagingException _) {
            result.setResult(EMailStatus.ERROR.ordinal());

            response.setMessage("Error occurred when sending email.");
            response.setErrors(HttpStatus.INTERNAL_SERVER_ERROR);
            response.setResult(result);
        }
        return response;
    }

    private String createForgetPasswordUrl(String email) {
        String uuid = Generator.generateUUID();
        String url = "";

        Optional<UserResetPassword> activationOpt = resetPasswordRepo.findByUserEmailAndUsed(email, false);

        activationOpt.ifPresent(resetPasswordRepo::delete);

        Optional<Users> userOpt = userRepo.findByEmail(email);

        if (userOpt.isPresent()) {
            UserResetPassword resetPassword = new UserResetPassword();
            resetPassword.setId(0L);
            resetPassword.setUser(userOpt.get());
            resetPassword.setUuid(uuid);
            resetPassword.setUsed(false);
            resetPasswordRepo.save(resetPassword);

            url = siteUrl + "reset?forgetKey=" + uuid + "&to=" + email;
        }
        return url;
    }
}
