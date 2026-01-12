package com.glycemic.entity.repository.glycemic;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.glycemic.entity.model.glycemic.UserResetPassword;
import org.springframework.stereotype.Repository;

@Repository
public interface UserResetPasswordRepository extends JpaRepository<UserResetPassword, Long> {

    Optional<UserResetPassword> findByUserEmail(String email);

    Optional<UserResetPassword> findByUserEmailAndUsed(String email, Boolean used);

    Optional<UserResetPassword> findByUserEmailAndUuid(String email, String uuid);
}
