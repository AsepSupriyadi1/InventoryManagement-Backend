package com.cpl.jumpstart.dto.request;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {
    private String email;
    private String password;
    private String fullName;
}
