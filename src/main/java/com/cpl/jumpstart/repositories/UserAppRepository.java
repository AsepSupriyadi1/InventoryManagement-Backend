package com.cpl.jumpstart.repositories;

import com.cpl.jumpstart.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAppRepository extends JpaRepository<UserApp, Long> {


    @Query(value = "SELECT MAX(u.userId) FROM UserApp u")
    Long findMaxId();

    Optional<UserApp> findByEmail(String email);


    @Query("SELECT u FROM UserApp u WHERE u.userRole = 'STORE_ADMIN'")
    List<UserApp> findALlStaff();

    @Query("SELECT u FROM UserApp u LEFT JOIN u.outlet o WHERE u.userRole = 'STORE_ADMIN' AND o IS NULL")
    List<UserApp> findStaffWithoutOutlet();



}
