package com.cpl.jumpstart.services;


import com.cpl.jumpstart.entity.Token;
import com.cpl.jumpstart.repositories.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler  {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        // ambil header dari request nya
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        // cek jika header nya ada dan dimulai dengan kata "Bearer " atau tidak
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }


        jwt = authHeader.substring(7); // dimulai dari 7 karena kata "Bearer " itu jumlahnya 7
        Token storedToken = tokenRepository.findByToken(jwt).orElseThrow(() -> new RuntimeException("Token null"));

        if(storedToken != null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }

    }
}
