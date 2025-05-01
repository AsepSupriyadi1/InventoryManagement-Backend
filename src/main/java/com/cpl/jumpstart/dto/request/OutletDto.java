package com.cpl.jumpstart.dto.request;


import com.cpl.jumpstart.entity.UserApp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutletDto {
    private String outletName;
    private String outletCode;
    private String phoneNumber;
    private String outletAddress;
    private String userId;
}
