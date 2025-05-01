package com.cpl.jumpstart.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_product_purchases")
public class ProductPurchases {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productPurchasesId;
    private String productName;
    private String productId;
    private int quantity;



    @ManyToOne
    @JoinColumn(name = "purchase_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Purchases purchases;

}
