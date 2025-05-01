package com.cpl.jumpstart.dto.request;


import com.cpl.jumpstart.entity.Outlet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private String customerFullName;
    private String phoneNumber;
    private String address;
    private String email;
    private String outletId;

}
