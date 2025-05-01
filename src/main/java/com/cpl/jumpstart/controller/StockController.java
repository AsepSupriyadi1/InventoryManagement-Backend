package com.cpl.jumpstart.controller;


import com.cpl.jumpstart.dto.request.StockProductRequest;
import com.cpl.jumpstart.dto.response.MessageResponse;
import com.cpl.jumpstart.entity.StockProduct;
import com.cpl.jumpstart.model.StocksModel;
import com.cpl.jumpstart.repositories.StockProductRepository;
import com.cpl.jumpstart.services.StockProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stock")
public class StockController {


    @Autowired
    private StockProductService stockProductService;

    @Autowired
    private StockProductRepository stockProductRepository;

    @PostMapping
    public ResponseEntity<MessageResponse> addNewStockProduct(@RequestBody StockProductRequest stockProductRequest) {

        try {

            StockProduct stockProduct = new StockProduct();
            stockProduct.setMinimumStockLevel(stockProductRequest.getMinimumStockLevel());
            stockProduct.setMaximumStockLevel(stockProductRequest.getMaximumStockLevel());


            if(!stockProductRequest.getStoksId().equals("undefined")){
                StockProduct stockProductDetails = stockProductService.findById(Long.parseLong(stockProductRequest.getStoksId()));
                stockProductDetails.setMinimumStockLevel(stockProductRequest.getMinimumStockLevel());
                stockProductDetails.setMaximumStockLevel(stockProductRequest.getMaximumStockLevel());
                stockProductRepository.save(stockProductDetails);
                return ResponseEntity.ok(new MessageResponse("Stock strategy updated successfully !"));
            }

            stockProductService.addNewStock(stockProduct, stockProductRequest.getOutletName(), stockProductRequest.getProductName());

            return ResponseEntity.ok(new MessageResponse("Stock strategy added successfully"));

        } catch (Exception e) {

            MessageResponse messageResponse = new MessageResponse();

            if (e.getMessage().equals("ZERO")) {
                messageResponse.setMessage("Cannot set Minimum and Maximum level to 0 !");
            } else if (e.getMessage().equals("LESTLEVEL")) {
                messageResponse.setMessage("Maximum should cannot less than minimum level !");
            } else {
                messageResponse.setMessage("Id Not Found !");
            }

            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
        }

    }

    @GetMapping
    public ResponseEntity<List<StockProduct>> getAllStock() {
        List<StockProduct> stockProductList = stockProductRepository.findAll();
        return ResponseEntity.ok(stockProductList);
    }

    @GetMapping("/all-stock-level")
    public ResponseEntity<List<StocksModel>> getAllStockLevel() {
        List<String[]> stockProductList = stockProductService.findAllStockLevel();

        List<StocksModel> returnList = new ArrayList<>();


        for (String[] strings : stockProductList) {

            StocksModel stocksModel = new StocksModel();
            stocksModel.setProductName(strings[0]);
            stocksModel.setOutletName(strings[1]);
            stocksModel.setCurrentQuantity(strings[2] != null ? Integer.parseInt(strings[2]) : 0);
            stocksModel.setMinStockLevelQuantity(strings[3] != null ? Integer.parseInt(strings[3]) : 0);
            stocksModel.setMaxStockLevelQuantity(strings[4] != null ? Integer.parseInt(strings[4]) : 0);
            stocksModel.setStocksId(strings[5] != null ? strings[5] : "undefined");
            returnList.add(stocksModel);
        }

        return ResponseEntity.ok(returnList);
    }

    @GetMapping("/all-outlet-stock-level/{outletId}")
    public ResponseEntity<List<StocksModel>> getAllOutletStockLevel(
            @PathVariable(name = "outletId") Long outletId
    ) {

        List<String[]> stockProductList = new ArrayList<>();

        try {
            stockProductList = stockProductService.findAllStockLevelByOutlets(outletId);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.ok(null);
        }

        List<StocksModel> returnList = new ArrayList<>();

        for (String[] strings : stockProductList) {

            StocksModel stocksModel = new StocksModel();
            stocksModel.setProductName(strings[0]);
            stocksModel.setOutletName(strings[1]);
            stocksModel.setCurrentQuantity(strings[2] != null ? Integer.parseInt(strings[2]) : 0);
            stocksModel.setMinStockLevelQuantity(strings[3] != null ? Integer.parseInt(strings[3]) : 0);
            stocksModel.setMaxStockLevelQuantity(strings[4] != null ? Integer.parseInt(strings[4]) : 0);
            stocksModel.setStocksId(strings[5] != null ? strings[5] : "undefined");
            returnList.add(stocksModel);
        }

        return ResponseEntity.ok(returnList);
    }


    @DeleteMapping("/delete/{stockId}")
    public ResponseEntity<MessageResponse> deleteStockStrategy(@PathVariable(name = "stockId") Long stockId) {
        try {
            stockProductService.deleteStockById(stockId);
            return ResponseEntity.ok(new MessageResponse(String.format("Stock strategy with id %s has been deleted", stockId)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/detail/{stockId}")
    public ResponseEntity<?> getStockDetail(@PathVariable(name = "stockId") Long stockId) {
        try {
            StockProduct stockProduct = stockProductService.findById(stockId);
            return ResponseEntity.ok(stockProduct);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(String.format("Stock not found for id %s", stockId));
        }
    }

    @PutMapping("/update/{stockId}")
    public ResponseEntity<MessageResponse> updateStock(@PathVariable(name = "stockId") Long stockId, @RequestBody StockProductRequest stockProductRequest) {
        try {

            StockProduct stockProduct = new StockProduct();
            stockProduct.setMaximumStockLevel(stockProductRequest.getMaximumStockLevel());
            stockProduct.setMinimumStockLevel(stockProductRequest.getMinimumStockLevel());
            stockProductService.updateStock(stockProduct, stockId);


            return ResponseEntity.ok(new MessageResponse("Stock level updated successfully !"));

        } catch (Exception e) {
            MessageResponse messageResponse = new MessageResponse();

            if (e.getMessage().equals("ZERO")) {
                messageResponse.setMessage("Cannot set Minimum and Maximum level to 0 !");
            } else if (e.getMessage().equals("LESTLEVEL")) {
                messageResponse.setMessage("Maximum should cannot less than minimum level !");
            }

            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
        }
    }


}
