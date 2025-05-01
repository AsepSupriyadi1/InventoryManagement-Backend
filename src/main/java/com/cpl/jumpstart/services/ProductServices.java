package com.cpl.jumpstart.services;


import com.cpl.jumpstart.dto.request.AddProductRequest;
import com.cpl.jumpstart.entity.Product;
import com.cpl.jumpstart.entity.ProductCategory;
import com.cpl.jumpstart.entity.Supplier;
import com.cpl.jumpstart.repositories.ProductCategoryRepository;
import com.cpl.jumpstart.repositories.ProductRepository;
import com.cpl.jumpstart.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServices {

    @Autowired
    private ProductCategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;


    // -=-=-=-=-=-=-=-=-=-=--=-=-=-= PRODUCT  SERVICES --=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=

    public void addProduct(Product product, String supplierId, String categoryId) {

        if(!supplierId.equals("null")) {
            Supplier supplier = supplierRepository.findById(Long.parseLong(supplierId)).orElseThrow(
                    () -> new RuntimeException("Supplier not found " + supplierId)
            );
            product.setSupplier(supplier);
            

        }

        if(!categoryId.equals("null")) {
            ProductCategory category = categoryRepository.findById(Long.parseLong(categoryId)).orElseThrow(
                    () -> new RuntimeException("Category not found " + categoryId)
            );
            product.setCategory(category);
        }


        productRepository.save(product);

    }

    public Product findProductById(Long productId){
        return productRepository.findById(productId).orElseThrow(
                () -> new RuntimeException(String.format("Product with id %s not found", productId))
        );
    }

    public void updateProduct(Product updatedProduct){
       productRepository.save(updatedProduct);
    }

    public void deleteById(Long productId){
        productRepository.deleteById(productId);
    }


    public List<Product> findALlBySupplier(Long supplierId){

        try {
//            Supplier supplier = supplierRepository.findById(supplierId).orElseThrow(
//                    () -> new RuntimeException("Supplier Not Found !")
//            );

            return productRepository.findAllByProductBySupplier(supplierId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Supplier Not Found");
        }

    }

    // -=-=-=-=-=-=-=-=-=-=--=-=-=-= END OF PRODUCT SERVICES --=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=



    // -=-=-=-=-=-=-=-=-=-=--=-=-=-= CATEGORY SERVICES --=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=


    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public List<ProductCategory> getAllProductsCategory(){
        return categoryRepository.findAll();
    }


    public Product findByProductName(String productName){

       return  productRepository.findByProductName(productName);

    }


    public ProductCategory findCategoryById(Long categoryId){
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new RuntimeException(String.format("Category with id %s not found", categoryId))
        );
    }

    // -=-=-=-=-=-=-=-=-=-=--=-=-=-= END OF CATEGORY SERVICES --=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=


}
