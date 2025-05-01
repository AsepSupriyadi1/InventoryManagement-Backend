package com.cpl.jumpstart.repositories;

import com.cpl.jumpstart.entity.PaymentToken;
import com.cpl.jumpstart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentTokenRepository extends JpaRepository<PaymentToken, Long> {


    @Query("SELECT p FROM PaymentToken p WHERE p.token = :token")
    PaymentToken findByToken(String token);

}
