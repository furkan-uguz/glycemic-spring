package com.glycemic.entity.repository.glycemic;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glycemic.services.auth.util.ERoleTypes;
import com.glycemic.entity.model.glycemic.Roles;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByName(ERoleTypes name);
}