package com.cpl.jumpstart.repositories;


import com.cpl.jumpstart.entity.Product;
import com.cpl.jumpstart.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query(value = "SELECT MAX(s.supplierId) FROM Supplier s")
    Long findMaxId();


}
