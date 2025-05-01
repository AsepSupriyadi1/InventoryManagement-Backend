package com.cpl.jumpstart.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUser {

    private Long userId;
    private String email;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String role;
    private String outletId;

}
