package com.cpl.jumpstart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Double prices;

    @Column(nullable = false)
    private Double Costs;

    @Column(nullable = false)
    private String productDesc;

    @Column(nullable = false)
    private Date datetime;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="product_pic", columnDefinition = "LONGBLOB", nullable = false)
    private byte[] productPic;


    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<StockProduct> stockProducts;


    // -=-=-=-=-=-=-=-=-= DEPENDECIES -=-=-=-=-=-=-=-=-=-=-=-=-=-=
    @ManyToOne
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private ProductCategory category;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Supplier supplier;

}
