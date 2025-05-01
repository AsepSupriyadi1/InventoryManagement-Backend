package com.cpl.jumpstart.services;


import com.cpl.jumpstart.entity.PaymentToken;
import com.cpl.jumpstart.repositories.PaymentTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class PaymentTokenService {



    @Autowired
    private PaymentTokenRepository paymentTokenRepository;


    public void saveToken(PaymentToken paymentToken){
        paymentTokenRepository.save(paymentToken);
    }


    public PaymentToken findByToken(String token) {

        PaymentToken paymentToken = paymentTokenRepository.findByToken(token);
        if(paymentToken == null){
            throw new RuntimeException("Token not found");
        }
        return paymentToken;
    }

    public PaymentToken findById(Long tokenId){

        return paymentTokenRepository.findById(tokenId).orElseThrow(
                () -> new RuntimeException("Token Not Found")
        );

    }

    public void deleteTokenById(Long tokenId){
        paymentTokenRepository.deleteById(tokenId);
    }






}
