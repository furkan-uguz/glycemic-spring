package com.glycemic.entity.repository.glycemiclog;

import com.glycemic.entity.model.glycemiclog.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

}
