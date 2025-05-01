package com.cpl.jumpstart.repositories;

import com.cpl.jumpstart.entity.CustomerProductPurchases;
import com.cpl.jumpstart.entity.ProductPurchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerProductPurchaseRepository extends JpaRepository<CustomerProductPurchases, Long> {

    @Query("SELECT u FROM CustomerProductPurchases u WHERE u.transaction.transactionId = :transactionId")
    List<CustomerProductPurchases> findAllByProductByTransactionId(Long transactionId);

}
