package com.cpl.jumpstart.repositories;

import com.cpl.jumpstart.entity.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PurchasesRepository extends JpaRepository<Purchases, Long> {


    @Query(value = "SELECT MAX(p.purchasesId) FROM from Purchases p ")
    Long findMaxId();

}
