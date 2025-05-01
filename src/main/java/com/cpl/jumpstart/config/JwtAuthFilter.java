package com.cpl.jumpstart.config;

import com.cpl.jumpstart.repositories.TokenRepository;
import com.cpl.jumpstart.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokensRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // ambil header dari request nya
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;


        // cek jika header nya ada dan dimulai dengan kata "Bearer " atau tidak
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); // dimulai dari 7 karena kata "Bearer " itu jumlahnya 7
        userEmail = jwtService.extractUsername(jwt); // to extract the user email from JWT token

        // Check if email is null dan user nya belum login
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // ambil user details dari database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);


            // check apakah tokennya valid di database atau ngk / di blacklist atau tidak
            // mencari token dengan token dan filter token nya yang tidak expired dan tidak revoked
            // jika tidak ada akan return false
            boolean isTokenValid = tokensRepository.findByToken(jwt).map(t ->
                            !t.isExpired() && !t.isRevoked())
                    .orElse(false);


            // check if user didalam token is valid or not
            if(jwtService.isTokenValid(jwt, userDetails) && isTokenValid){


                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userEmail, userDetails,userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Update the security context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }

            filterChain.doFilter(request, response);

        }
    }
}
