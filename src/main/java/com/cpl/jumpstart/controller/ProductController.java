package com.cpl.jumpstart.controller;


import com.cpl.jumpstart.dto.request.AddProductRequest;
import com.cpl.jumpstart.dto.response.MessageResponse;
import com.cpl.jumpstart.entity.Product;
import com.cpl.jumpstart.entity.ProductCategory;
import com.cpl.jumpstart.entity.Supplier;
import com.cpl.jumpstart.repositories.ProductCategoryRepository;
import com.cpl.jumpstart.repositories.SupplierRepository;
import com.cpl.jumpstart.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {


    @Autowired
    private ProductServices productServices;

    @Autowired
    private ProductCategoryRepository categoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;


    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- PRODUCT CONTROLLER -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
    @PostMapping
    public ResponseEntity<MessageResponse> addNewProduct(AddProductRequest request) throws IOException {

        Product product = new Product();
        product.setPrices(request.getPrices());
        product.setCosts(request.getCosts());
        product.setProductName(request.getProductName());
        product.setDatetime(new Date());
        product.setProductDesc(request.getProductDesc());

        if(request.getPicture() != null){
            product.setProductPic(request.getPicture().getBytes());
        }

        try {

            productServices.addProduct(product, request.getSupplierId(), request.getCategoryId());

        } catch (Exception e){

            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Add Product failed for " + request.getProductName()));

        }

        return ResponseEntity.ok(new MessageResponse("Add Product Success for " + request.getProductName()));

    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productServices.getAllProducts());
    }

    @GetMapping("/detail/{productId}")
    public ResponseEntity<Product> getDetailProduct(
            @PathVariable(name = "productId") Long productId
    ){
        try {
            Product product = productServices.findProductById(productId);
            return ResponseEntity.ok(product);

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<MessageResponse> updateProduct(
            @PathVariable(name = "productId") Long productId,
            @RequestParam(name = "productName") String productName,
            @RequestParam(name = "prices") Double prices,
            @RequestParam(name = "costs") Double costs,
            @RequestParam(name = "categoryId") String categoryId,
            @RequestParam(name = "supplierId") String supplierId,
            @RequestParam(name = "productDesc") String productDesc,
            @RequestParam(name = "picture") MultipartFile picture
    ){
        try {

            Product product = productServices.findProductById(productId);

            System.out.println(categoryId);
            System.out.println(supplierId);


            // CATEGORY RELATION
            if(categoryId.equals("null")){
                product.setCategory(null);
            } else {
                ProductCategory category = productServices.findCategoryById(Long.parseLong(categoryId));
                product.setCategory(category);
            }


            // SUPPLIER RELATION
            if(supplierId.equals("null")){
                product.setSupplier(null);
            } else {
                Supplier supplier = supplierRepository.findById(Long.parseLong(supplierId)).orElseThrow(
                        () -> new RuntimeException("Supplier Not Found")
                );
                product.setSupplier(supplier);
            }


            // PRODUCT DETAILS
            product.setProductName(productName);
            product.setProductDesc(productDesc);
            product.setPrices(prices);
            product.setCosts(costs);
            product.setProductDesc(productDesc);

            if(picture.getSize() != 0){
                product.setProductPic(picture.getBytes());
            }

            productServices.updateProduct(product);

            return ResponseEntity.ok(new MessageResponse("Product updated successfully !"));

        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<MessageResponse> deleteProduct(
            @PathVariable(name = "productId") Long productId
    ){

        try {
            productServices.deleteById(productId);
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Delete product failed for id " + productId));
        }
        return ResponseEntity.ok(new MessageResponse("Delete product Success for id" + productId));


    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<Product>> findAllProductBySupplier(
            @PathVariable(name = "supplierId") Long supplierId
    ){
        try {
            List<Product> productList = productServices.findALlBySupplier(supplierId);
            return ResponseEntity.ok(productList);

        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }




    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- END OF PRODUCT CONTROLLER -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-








    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- CATEGORY CONTROLLER -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-



    @GetMapping("/all-category")
    public ResponseEntity<List<ProductCategory>> getAllProductsCategory(){
        return ResponseEntity.ok(productServices.getAllProductsCategory());
    }

    @PostMapping("/category")
    public ResponseEntity<MessageResponse> addNewCategory(
            @RequestParam(name = "categoryName") String categoryName
    ) {

        ProductCategory category = new ProductCategory();
        category.setName(categoryName);

        try {
            categoryRepository.save(category);
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Add Category failed for " + categoryName));
        }

        return ResponseEntity.ok(new MessageResponse("Add Category Success for " + categoryName));

    }



    @PutMapping("/category/update/{categoryId}")
    public ResponseEntity<MessageResponse> updateCategory(
            @RequestParam(name = "categoryName") String categoryName,
            @PathVariable(name = "categoryId") Long categoryId
    ) {

        try {
            ProductCategory category = productServices.findCategoryById(categoryId);
            category.setName(categoryName);
            categoryRepository.save(category);

        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Update Category failed for " + categoryName));
        }

        return ResponseEntity.ok(new MessageResponse("Update Category Success for " + categoryName));

    }

    @GetMapping("/category/detail/{categoryId}")
    public ResponseEntity<ProductCategory> getCategoryById(@PathVariable(name = "categoryId") Long categoryId){
        try {
            ProductCategory productCategory = productServices.findCategoryById(categoryId);
            return ResponseEntity.ok(productCategory);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }


    @DeleteMapping("/category/delete/{categoryId}")
    public ResponseEntity<MessageResponse> deleteCategory(
            @PathVariable(name = "categoryId") Long categoryId
    ) {
        try {
            categoryRepository.deleteById(categoryId);
            return ResponseEntity.ok(new MessageResponse("Delete Category Success for id" + categoryId));
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Delete Category failed for id " + categoryId));
        }

    }

    // -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- END OF CATEGORY CONTROLLER -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-



}
