package com.cpl.jumpstart.entity;


import com.cpl.jumpstart.entity.constraint.EnumCountry;
import com.cpl.jumpstart.entity.constraint.UserAppRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_users")
public class UserApp implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    // -=--=-=-=- USER DETAILS -=-=-=-=-=-==
    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String staffCode;

    private String phoneNumber;
    private String address;

    // -=--=-=-=- DEPENDENCY -=-=-=-=-=-==
    @JsonIgnore
    @OneToOne(mappedBy = "userApp")
    private Outlet outlet;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserAppRole userRole;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Token> tokens;


    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }


    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
