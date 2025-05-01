package com.cpl.jumpstart.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto {

    // CONSTRAINT
    private String supplierId;
    private List<ProductPurchasesDto> listProduct;
    private String outletId;

}
