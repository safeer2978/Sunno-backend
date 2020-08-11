package com.sunno.accountservice.models.persistence.repository;

import com.sunno.accountservice.models.persistence.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("")
    User findByEmail(String email);

    @Query("")
    boolean existsByEmail(String email);
}