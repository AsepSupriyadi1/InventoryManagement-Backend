package com.cpl.jumpstart.repositories;

import com.cpl.jumpstart.entity.CustomerTransaction;
import com.cpl.jumpstart.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerTransactionRepository extends JpaRepository<CustomerTransaction, Long> {


    @Query(value = "SELECT MAX(c.transactionId) FROM from CustomerTransaction c ")
    Long findMaxId();

    @Query("SELECT c FROM CustomerTransaction c WHERE c.outlet.outletId = :outletId")
    List<CustomerTransaction> findAllTransactionByOutlet(Long outletId);

    @Query("SELECT c FROM CustomerTransaction c WHERE c.transactionStatus = 'PENDING'")
    List<CustomerTransaction> findPendingTransaction();


}
