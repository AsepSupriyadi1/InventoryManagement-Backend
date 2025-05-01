package com.cpl.jumpstart.repositories;

import com.cpl.jumpstart.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {


    @Query(value = "SELECT MAX(c.customerId) FROM Customer c")
    Long findMaxId();

}
