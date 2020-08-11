package com.sunno.accountservice.models.persistence.repository;

import com.sunno.accountservice.models.RoleName;
import com.sunno.accountservice.models.persistence.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
