package com.cpl.jumpstart.entity;

import com.cpl.jumpstart.entity.constraint.PurchasesStatus;
import com.cpl.jumpstart.entity.constraint.TransactionStatus;
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
@Table(name = "tb_customer_transaction")
public class CustomerTransaction {

    // -=-=-=-=-=- PURCHASES DETAILS -=-=-=-=-=-=-=
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private String transactionCode;

    @Column(nullable = false)
    private String dateTime;

    private String deliverStartDate;
    private String receiveDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus transactionStatus;


    // -=-=-=-=-=- SUPPLIER DETAILS -=-=-=-=-=-=-=
    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerCode;

    @Column(nullable = false)
    private String customerPhoneNumber;

    @Column(nullable = false)
    private String customerEmail;


    // -=-=-=-=-=- STAFF DETAILS -=-=-=-=-=-=-=
    @Column(nullable = false)
    private String staffName;

    @Column(nullable = false)
    private String staffCode;



    // -=-=-=-=-=- RELATION -=-=-=-=-=-=-=
    @JsonIgnore
    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerProductPurchases> productPurchasesList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "outlet_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Outlet outlet;



}
