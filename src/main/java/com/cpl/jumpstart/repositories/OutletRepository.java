package com.cpl.jumpstart.repositories;

import com.cpl.jumpstart.entity.Outlet;
import com.cpl.jumpstart.entity.Product;
import com.cpl.jumpstart.entity.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OutletRepository extends JpaRepository<Outlet, Long> {

    @Query("SELECT COUNT(o) FROM Outlet o")
    Long countAllOutlet();

    @Query("SELECT SUM(o.totalRevenue) FROM Outlet o")
    double getTotalRevenueAcrossOutlets();

    @Query("SELECT SUM(o.totalExpenses) FROM Outlet o")
    double getTotalExpensesAcrossOutlets();

    @Query(value = "SELECT MAX(o.outletId) FROM from Outlet o ")
    Long findMaxId();

    Optional<Outlet> findByUserApp(UserApp userApp);


    @Query("SELECT u FROM Outlet u WHERE u.outletName = :outletName")
    Outlet findByOutletName(String outletName);

}
