package com.glycemic.core.log;

import com.glycemic.entity.model.glycemiclog.LoginLog;
import com.glycemic.entity.repository.glycemiclog.LoginLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional("logTransactionManager")
public class LoginLogManager {

    private final LoginLogRepository loginLogRepo;

    public void loginLog(Long userId) {
        LoginLog login = new LoginLog();
        login.setUserId(userId);
        loginLogRepo.save(login);
    }
}