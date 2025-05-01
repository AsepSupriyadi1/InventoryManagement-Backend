package com.cpl.jumpstart.services;


import com.cpl.jumpstart.entity.Token;
import com.cpl.jumpstart.entity.UserApp;
import com.cpl.jumpstart.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokensRepository;

    public void revokeAllUserTokens(UserApp user){

        List<Token> validAllUserTokens = tokensRepository.findAllValidTokensByUser(user.getUserId());

        if(validAllUserTokens.isEmpty()){
            return;
        }

        validAllUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokensRepository.saveAll(validAllUserTokens);

    }

}
