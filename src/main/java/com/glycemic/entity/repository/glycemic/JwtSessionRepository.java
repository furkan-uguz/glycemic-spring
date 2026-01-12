package com.glycemic.entity.repository.glycemic;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glycemic.entity.model.glycemic.JwtSession;
import com.glycemic.entity.model.glycemic.Users;

@Repository
public interface JwtSessionRepository extends JpaRepository<JwtSession, Long> {

    Optional<JwtSession> findByUsers(Users users);

    List<JwtSession> findAllByUsers(Users users);

    Optional<JwtSession> findByJwttoken(String jwttoken);
}
