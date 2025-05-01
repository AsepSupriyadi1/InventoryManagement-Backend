package com.cpl.jumpstart.repositories;

import com.cpl.jumpstart.entity.Outlet;
import com.cpl.jumpstart.entity.Product;
import com.cpl.jumpstart.entity.StockProduct;
import com.cpl.jumpstart.model.StocksModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockProductRepository extends JpaRepository<StockProduct, Long> {

    List<StockProduct> findAllStockProductByOutlet(Outlet outlet);

    Optional<StockProduct> findByOutletAndProduct(Outlet outlet, Product product);

    @Query(value = "SELECT p.product_name, o.outlet_name, s.current_quantity, s.minimum_stock_level, s.maximum_stock_level, s.stock_id FROM tb_products as p CROSS JOIN tb_outlets as o LEFT JOIN tb_stocks s ON p.product_id = s.product_id AND o.outlet_id = s.outlet_id ORDER BY outlet_name;", nativeQuery = true)
    List<String[]> findAllStocksUnitItem();

    @Query(value = "SELECT p.product_name, o.outlet_name, s.current_quantity, s.minimum_stock_level, s.maximum_stock_level, s.stock_id FROM tb_products as p CROSS JOIN tb_outlets as o LEFT JOIN tb_stocks s ON p.product_id = s.product_id AND o.outlet_id = s.outlet_id WHERE o.outlet_id = :outletId ORDER BY outlet_name;", nativeQuery = true)
    List<String[]> findAllStocksUnitItemByOutlets(Long outletId);

    @Query(value = "SELECT * FROM tb_stocks as s WHERE s.outlet_id = :outletId AND product_id = :productId", nativeQuery = true)
    StockProduct findStockByProductAndOutlet(Long productId, Long outletId);

    List<StockProduct> findListByOutletAndProduct(Outlet outlet, Product product);

}
