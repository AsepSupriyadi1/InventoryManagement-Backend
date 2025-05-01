package com.cpl.jumpstart.entity;


import com.cpl.jumpstart.entity.constraint.PurchasesStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_purchases")
public class Purchases {


    // -=-=-=-=-=- PURCHASES DETAILS -=-=-=-=-=-=-=
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchasesId;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private String purchaseCode;

    @Column(nullable = false)
    private String dateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchasesStatus purchasesStatus;

    // OTHERS
    private String approvedBy;
    private String receivedDate;
    private String payedAt;

    // -=-=-=-=-=- SUPPLIER DETAILS -=-=-=-=-=-=-=
    @Column(nullable = false)
    private String supplierName;

    @Column(nullable = false)
    private String supplierCode;

    @Column(nullable = false)
    private String supplierPhoneNumber;

    @Column(nullable = false)
    private String supplierEmail;


    // -=-=-=-=-=- STAFF DETAILS -=-=-=-=-=-=-=
    @Column(nullable = false)
    private String staffName;

    @Column(nullable = false)
    private String staffCode;




    // -=-=-=-=-=- RELATION -=-=-=-=-=-=-=
    @JsonIgnore
    @OneToMany(mappedBy = "purchases", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductPurchases> productPurchasesList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "outlet_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Outlet outlet;

}
