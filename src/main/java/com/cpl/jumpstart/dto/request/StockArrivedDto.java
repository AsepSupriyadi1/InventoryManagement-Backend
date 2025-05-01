package com.cpl.jumpstart.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockArrivedDto {

    private String purchaseId;
    private Date arrivedDate;

}
