package com.sunno.accountservice.models.persistence.repository;

import com.sunno.accountservice.models.persistence.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {


    @Query("FROM Subscription WHERE user_id = :user_id ")
    Subscription findByUserId(@Param("user_id") long user_id);
}
