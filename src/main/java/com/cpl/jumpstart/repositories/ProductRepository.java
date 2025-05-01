package com.cpl.jumpstart.repositories;

import com.cpl.jumpstart.entity.Product;
import com.cpl.jumpstart.entity.Supplier;
import com.cpl.jumpstart.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {


    @Query("SELECT COUNT(p) FROM Product p")
    Long countAllProduct();

    @Query("SELECT u FROM Product u WHERE u.supplier.supplierId = :supplierId")
    List<Product> findAllByProductBySupplier(Long supplierId);


    @Query("SELECT u FROM Product u WHERE u.productName = :productName")
    Product findByProductName(String productName);

}
