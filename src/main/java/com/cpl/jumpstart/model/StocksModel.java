package com.cpl.jumpstart.model;


import lombok.*;
import org.springframework.data.domain.Auditable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StocksModel {
    private String stocksId;
    private String productName;
    private String outletName;
    private int currentQuantity;
    private int minStockLevelQuantity;
    private int maxStockLevelQuantity;
}
