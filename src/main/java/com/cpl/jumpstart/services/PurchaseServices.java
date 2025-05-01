package com.cpl.jumpstart.services;


import com.cpl.jumpstart.dto.request.ProductPurchasesDto;
import com.cpl.jumpstart.dto.request.PurchaseDto;
import com.cpl.jumpstart.dto.response.BillsInfoDto;
import com.cpl.jumpstart.entity.*;
import com.cpl.jumpstart.entity.constraint.PurchasesStatus;
import com.cpl.jumpstart.repositories.OutletRepository;
import com.cpl.jumpstart.repositories.ProductPurchasesRepository;
import com.cpl.jumpstart.repositories.PurchasesRepository;
import com.cpl.jumpstart.repositories.StockProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PurchaseServices {


    @Autowired
    public PurchasesRepository purchasesRepo;

    @Autowired
    public ProductPurchasesRepository productPurchasesRepo;

    @Autowired
    public StockProductRepository stockProductRepo;

    @Autowired
    public ProductServices productServices;

    @Autowired
    private OutletService outletService;

    @Autowired
    private OutletRepository outletRepository;

    @Autowired
    private StockProductService stockProductService;
    @Autowired
    private SupplierServices supplierServices;

    @Autowired
    private UserAppServices userAppServices;


    public BillsInfoDto addNewPurchase(
            PurchaseDto purchaseDto
    ) {

        Purchases purchases = new Purchases();
        UserApp staff = userAppServices.getCurrentUser();
        Outlet outlet;

        try {
            outlet = outletService.findById(Long.parseLong(purchaseDto.getOutletId()));
            purchases.setOutlet(outlet);

            Supplier supplier = supplierServices.findById(Long.parseLong(purchaseDto.getSupplierId()));
            purchases.setSupplierCode(supplier.getSupplierCode());
            purchases.setSupplierName(supplier.getSupplierName());
            purchases.setSupplierPhoneNumber(supplier.getPhoneNumber());
            purchases.setSupplierEmail(supplier.getEmail());
        } catch (Exception e) {
            throw new RuntimeException("CONSTRAINT_NOT_FOUND");
        }


        List<ProductPurchases> purchasedProductList = new ArrayList<>();
        double totalAmount = 0;


        if(purchaseDto.getListProduct().size() == 0){
            throw new RuntimeException("NO_ITEMS");
        }


        for (ProductPurchasesDto requestProduct : purchaseDto.getListProduct()) {

            Product product;

            try {
                product = productServices.findProductById(requestProduct.getProductId());
            } catch (Exception e){
                throw new RuntimeException("PRODUCT_NOT_FOUND");
            }

            ProductPurchases productPurchases = new ProductPurchases();
            productPurchases.setProductName(product.getProductName());
            productPurchases.setProductId(product.getProductId().toString());
            productPurchases.setPurchases(purchases);

            StockProduct productRules = stockProductService.findByProductAndOutlet(product.getProductId(), outlet.getOutletId());


            if(requestProduct.getQuantity() == 0){
                throw new RuntimeException("ZERO_VALUE");
            }


            if(productRules != null){

                boolean isMinimumStockLevel = requestProduct.getQuantity() >= productRules.getMinimumStockLevel();
                boolean isGreaterThanMaximum = requestProduct.getQuantity() > productRules.getMaximumStockLevel();
                boolean isMax = requestProduct.getQuantity() + productRules.getCurrentQuantity() > productRules.getMaximumStockLevel();


                if (!isMinimumStockLevel) {
                    throw new RuntimeException("MINIMUM_ERROR");
                }

                if (isGreaterThanMaximum) {
                    throw new RuntimeException("GREATER_ERROR");
                }

                if (isMax) {
                    throw new RuntimeException("MAX_ERROR");
                }
            }

            productPurchases.setQuantity(requestProduct.getQuantity());

            // ADD TO LIST
            purchasedProductList.add(productPurchases);

            // CALCULATE THE PRICE
            totalAmount += product.getPrices() * requestProduct.getQuantity();

        }


        // USER DETAILS
        purchases.setStaffName(staff.getFullName());
        purchases.setStaffCode(staff.getStaffCode());

        // PURCHASES DETAIL
        purchases.setTotalAmount(totalAmount);


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        purchases.setDateTime(format.format(new Date()));

        purchases.setPurchasesStatus(PurchasesStatus.PENDING);

        Long puchaseCode = getLastSavedPurchasesId();
        if(puchaseCode == null) {
            puchaseCode = 1L;
        } else {
            puchaseCode += 1;
        }
        purchases.setPurchaseCode("BILL-JP-" + puchaseCode);

       return new BillsInfoDto(purchases, purchasedProductList);
    }



    public void saveBills(BillsInfoDto billsInfoDto){
        purchasesRepo.save(billsInfoDto.getPurchases());

        for(ProductPurchases productPurchases : billsInfoDto.getPurchasesList()){
            ProductPurchases items = new ProductPurchases();
            items.setQuantity(productPurchases.getQuantity());
            items.setPurchases(billsInfoDto.getPurchases());
            items.setProductId(productPurchases.getProductId());
            items.setProductName(productPurchases.getProductName());
            productPurchasesRepo.save(items);
        }

    }

    public void approveBills(String purchaseId){
        UserApp userApp = userAppServices.getCurrentUser();
        Purchases purchases = findPurchaseById(Long.parseLong(purchaseId));
        purchases.setPurchasesStatus(PurchasesStatus.APPROVED);
        purchases.setApprovedBy(userApp.getStaffCode());
        purchasesRepo.save(purchases);
    }

    public void goodsArrived(String purchaseId, Date dateArrived){
        Purchases purchases = findPurchaseById(Long.parseLong(purchaseId));
        Outlet outlet = purchases.getOutlet();


        for(ProductPurchases productPurchases : purchases.getProductPurchasesList()){
            Product product = productServices.findProductById(Long.parseLong(productPurchases.getProductId()));
            Optional<StockProduct> stockProduct = stockProductRepo.findByOutletAndProduct(outlet, product);

            if(stockProduct.isEmpty()){
                StockProduct newStock = new StockProduct();
                newStock.setCurrentQuantity(productPurchases.getQuantity());
                newStock.setProduct(product);
                newStock.setOutlet(outlet);
                stockProductRepo.save(newStock);
                System.out.println("Empty");
            } else {
                int plusQuantity = stockProduct.get().getCurrentQuantity() + productPurchases.getQuantity();
                stockProduct.get().setCurrentQuantity(plusQuantity);
                stockProductRepo.save(stockProduct.get());
            }
        }

        purchases.setPurchasesStatus(PurchasesStatus.ARRIVED);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        purchases.setReceivedDate(format.format(dateArrived));
        purchasesRepo.save(purchases);
    }


    public Purchases findPurchaseById(Long purchaseId){

        return purchasesRepo.findById(purchaseId).orElseThrow(
                () -> new RuntimeException(String.format("Purchase not found for id %s", purchaseId))
        );

    }


    public void makePayment(Long purchaseId){
        Purchases purchases = findPurchaseById(purchaseId);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        purchases.setPayedAt(format.format(new Date()));
        purchases.setPurchasesStatus(PurchasesStatus.COMPLETED);

        Outlet outlet = purchases.getOutlet();
        double totalExpenses = outlet.getTotalExpenses() + purchases.getTotalAmount();
        outlet.setTotalExpenses(totalExpenses);
        outletRepository.save(outlet);

        purchasesRepo.save(purchases);
    }

    public Long getLastSavedPurchasesId() {
        return purchasesRepo.findMaxId();
    }



}
