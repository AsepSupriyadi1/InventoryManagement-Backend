package com.cpl.jumpstart.dto.response;

import com.cpl.jumpstart.entity.ProductPurchases;
import com.cpl.jumpstart.entity.Purchases;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillsInfoDto {
    private Purchases purchases;
    private List<ProductPurchases> purchasesList;
}
