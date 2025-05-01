package com.cpl.jumpstart.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_supplier")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;

    @Column(nullable = false, unique = true)
    private String supplierCode;

    @Column(nullable = false)
    private String supplierName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String phoneNumber;


    @Column(nullable = false)
    private String email;


    @JsonIgnore
    @OneToMany(mappedBy = "supplier")
    private List<Product> products;
}
