package com.cpl.jumpstart.controller;


import com.cpl.jumpstart.entity.CustomerTransaction;
import com.cpl.jumpstart.repositories.CustomerTransactionRepository;
import com.cpl.jumpstart.repositories.OutletRepository;
import com.cpl.jumpstart.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {


    @Autowired
    private OutletRepository outletRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerTransactionRepository transactionRepository;


    @GetMapping
    public ResponseEntity<Map<String, Long>> dashboard(){

        Long countAllOutlet = outletRepository.countAllOutlet();
        Long countAllProduct = productRepository.countAllProduct();

        Map<String, Long> listDashboardInfo = new HashMap<>();

        listDashboardInfo.put("totalOutlet", countAllOutlet);
        listDashboardInfo.put("totalProduct", countAllProduct);
        return ResponseEntity.ok(listDashboardInfo);

    }


    @GetMapping("/all-pending-transaction")
    public ResponseEntity<List<CustomerTransaction>> getPendingTransaction(){
        List<CustomerTransaction> listPendingTransaction = transactionRepository.findPendingTransaction();
        return ResponseEntity.ok(listPendingTransaction);
    }


    @GetMapping("/dollars")
    public ResponseEntity<Map<String, Double>> getTotalRevenue(){
       double totalRevenue = outletRepository.getTotalRevenueAcrossOutlets();
       double totalExpenses = outletRepository.getTotalExpensesAcrossOutlets();

        Map<String, Double> listDashboardInfo = new HashMap<>();

        listDashboardInfo.put("totalRevenue", totalRevenue);
        listDashboardInfo.put("totalExpenses", totalExpenses);

        return ResponseEntity.ok(listDashboardInfo);
    }



}
