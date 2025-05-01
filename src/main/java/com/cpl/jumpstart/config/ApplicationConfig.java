package com.cpl.jumpstart.config;

import com.cpl.jumpstart.repositories.UserAppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserAppRepository repository;

    @Bean
    UserDetailsService userDetailsService(){
        return username -> repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider dProvider = new DaoAuthenticationProvider();
        dProvider.setUserDetailsService(userDetailsService());
        dProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return dProvider;
    }


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    PasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    // Used by Spring Security if CORS is enabled.
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Menentukan daftar origin yang diizinkan
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Menentukan metode yang diizinkan

        configuration.setAllowedHeaders(Arrays.asList("*"));  // Menentukan header yang diizinkan
        configuration.setAllowCredentials(true);    // Mengizinkan kredensial otentikasi

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Mengaplikasikan konfigurasi ke semua URL

        return source;
    }




}
