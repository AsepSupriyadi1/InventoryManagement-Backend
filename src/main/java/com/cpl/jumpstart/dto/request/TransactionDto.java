package com.cpl.jumpstart.dto.request;

import com.cpl.jumpstart.entity.CustomerProductPurchases;
import com.cpl.jumpstart.entity.CustomerTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    private String customerId;
    private List<TransactionProductDto> productDtoList;
    private String outletId;

}
