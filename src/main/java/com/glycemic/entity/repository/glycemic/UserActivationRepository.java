package com.glycemic.entity.repository.glycemic;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glycemic.entity.model.glycemic.UserActivation;

@Repository
public interface UserActivationRepository extends JpaRepository<UserActivation, Long> {

    Optional<UserActivation> findByUserEmail(String email);

    Optional<UserActivation> findByUserEmailAndUuid(String email, String uuid);
}
