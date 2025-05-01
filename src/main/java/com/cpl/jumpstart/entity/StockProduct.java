package com.cpl.jumpstart.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_stocks")
public class StockProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "outlet_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Outlet outlet;

    @Column(nullable = false)
    private int minimumStockLevel;

    @Column(nullable = false)
    private int maximumStockLevel;

    @Column(nullable = false)
    private int currentQuantity;
}
