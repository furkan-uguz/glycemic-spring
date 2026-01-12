package com.glycemic.services.mail.controller;

import com.glycemic.core.model.MainResponse;
import com.glycemic.services.mail.model.forget.ForgetMailRequest;
import com.glycemic.services.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;

    @PostMapping(path = "/forget", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MainResponse> forgetMail(@RequestBody ForgetMailRequest request) {
        MainResponse body = mailService.forgetPassword(request);

        return new ResponseEntity<>(body, body.getErrors());
    }
}
