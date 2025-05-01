package com.cpl.jumpstart.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_payment_token")
public class PaymentToken {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passwordResetTokenId;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String token;

    @Column(nullable = false)
    private Long transactionId;



}
