package com.cpl.jumpstart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockProductRequest {
    private String stoksId;
    private String productName;
    private String outletName;
    private int minimumStockLevel;
    private int maximumStockLevel;
}
