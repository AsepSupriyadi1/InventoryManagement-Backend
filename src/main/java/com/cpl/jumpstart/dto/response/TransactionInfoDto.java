package com.cpl.jumpstart.dto.response;


import com.cpl.jumpstart.entity.CustomerProductPurchases;
import com.cpl.jumpstart.entity.CustomerTransaction;
import com.cpl.jumpstart.entity.ProductPurchases;
import com.cpl.jumpstart.entity.Purchases;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInfoDto {
    private CustomerTransaction transaction;
    private List<CustomerProductPurchases> purchasesList;

}
