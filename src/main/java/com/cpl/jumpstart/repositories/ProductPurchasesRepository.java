package com.cpl.jumpstart.repositories;

import com.cpl.jumpstart.entity.Product;
import com.cpl.jumpstart.entity.ProductPurchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductPurchasesRepository extends JpaRepository<ProductPurchases, Long> {


    @Query("SELECT u FROM ProductPurchases u WHERE u.purchases.purchasesId = :purchasesId")
    List<ProductPurchases> findAllByProductByPurchases(Long purchasesId);

}
