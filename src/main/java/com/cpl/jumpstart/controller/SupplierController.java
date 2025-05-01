package com.cpl.jumpstart.controller;


import com.cpl.jumpstart.dto.response.MessageResponse;
import com.cpl.jumpstart.entity.Supplier;
import com.cpl.jumpstart.repositories.SupplierRepository;
import com.cpl.jumpstart.services.SupplierServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/supplier")
public class SupplierController {
    private final SupplierRepository supplierRepo;
    private final SupplierServices supplierService;
    @Autowired
    public SupplierController(SupplierRepository supplierRepo, SupplierServices supplierService) {
        this.supplierRepo = supplierRepo;
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> getALlSupplier(){
        List<Supplier> supplierList = supplierRepo.findAll();
        return ResponseEntity.ok(supplierList);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> addNewSupplier(
        @RequestBody Supplier supplier
    ){

        try {
            supplierService.addNewSupplier(supplier);
            return ResponseEntity.ok(new MessageResponse("Supplier added successfully !"));
        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Null Column Identified"));
        }

    }


    @GetMapping("/detail/{supplierId}")
    public ResponseEntity<?> supplierDetail(@PathVariable(name = "supplierId") Long supplierId){

        try {
            Supplier supplierDetails = supplierService.findById(supplierId);
            return ResponseEntity.ok(supplierDetails);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Not Found"));
        }

    }

    @PutMapping("/update/{supplierId}")
    public ResponseEntity<MessageResponse> supplierDetail(
            @PathVariable(name = "supplierId") Long supplierId,
            @RequestBody Supplier supplier
    ){

        try {
            supplierService.updateSupplier(supplierId, supplier);
            return ResponseEntity.ok(new MessageResponse("Supplier updated successfully"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @DeleteMapping("/delete/{supplierId}")
    public ResponseEntity<MessageResponse> deleteSupplier(
            @PathVariable(name = "supplierId") Long supplierId
    ){

        try {
            supplierRepo.deleteById(supplierId);
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Delete supplier failed for id " + supplierId));
        }
        return ResponseEntity.ok(new MessageResponse("Delete supplier Success for id" + supplierId));


    }



}
