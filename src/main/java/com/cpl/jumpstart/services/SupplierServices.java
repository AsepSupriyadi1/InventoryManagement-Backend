package com.cpl.jumpstart.services;


import com.cpl.jumpstart.entity.Supplier;
import com.cpl.jumpstart.entity.constraint.EnumCountry;
import com.cpl.jumpstart.repositories.SupplierRepository;
import com.cpl.jumpstart.utils.CountryConfig;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierServices {


    @Autowired
    private SupplierRepository supplierRepository;

    public Long getLastSavedSupplierId() {
        return supplierRepository.findMaxId();
    }

    public void addNewSupplier(Supplier supplier){
        Long supplierCode = getLastSavedSupplierId();
        if(supplierCode == null) {
            supplierCode = 1L;
        } else {
            supplierCode += 1;
        }
        supplier.setSupplierCode("SUPPLIER-JP-" + supplierCode);
        supplierRepository.save(supplier);
    }

    public Supplier findById(Long supplierId){
       return supplierRepository.findById(supplierId).orElseThrow(() ->
               new RuntimeException(String.format("Supplier with id '%s' not found", supplierId))
       );
    }


    public void updateSupplier(Long supplierId, Supplier updatedSupplier){

        Supplier supplier = findById(supplierId);
        supplier.setSupplierName(updatedSupplier.getSupplierName());
        supplier.setAddress(updatedSupplier.getAddress());
        supplier.setCompanyName(updatedSupplier.getCompanyName());
        supplier.setPhoneNumber(updatedSupplier.getPhoneNumber());
        supplier.setEmail(updatedSupplier.getEmail());

        supplierRepository.save(supplier);

    }


}
