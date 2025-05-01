package com.cpl.jumpstart.entity;


import com.cpl.jumpstart.entity.constraint.EnumCountry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_outlets")
public class Outlet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;

    @Column(nullable = false)
    private String outletName;

    @Column(nullable = false, unique = true)
    private String outletCode;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String outletAddress;

    private boolean isOutletActive;

    private double totalRevenue;
    private double totalExpenses;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private UserApp userApp;

    @JsonIgnore
    @OneToMany(mappedBy = "outlet")
    private List<Customer> customerList;

    @JsonIgnore
    @OneToMany(mappedBy = "outlet")
    private List<StockProduct> stockProducts;

    @JsonIgnore
    @OneToMany(mappedBy = "outlet")
    private List<Purchases> purchases;

    @JsonIgnore
    @OneToMany(mappedBy = "outlet")
    private List<CustomerTransaction> customerTransactions;

}
