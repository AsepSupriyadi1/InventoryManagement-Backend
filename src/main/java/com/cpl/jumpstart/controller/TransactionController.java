package com.cpl.jumpstart.controller;


import com.cpl.jumpstart.dto.request.DeliveryDto;
import com.cpl.jumpstart.dto.request.PurchaseDto;
import com.cpl.jumpstart.dto.request.TransactionDto;
import com.cpl.jumpstart.dto.response.BillsInfoDto;
import com.cpl.jumpstart.dto.response.MessageResponse;
import com.cpl.jumpstart.dto.response.TransactionInfoDto;
import com.cpl.jumpstart.entity.*;
import com.cpl.jumpstart.repositories.CustomerTransactionRepository;
import com.cpl.jumpstart.services.EmailSenderService;
import com.cpl.jumpstart.services.PaymentTokenService;
import com.cpl.jumpstart.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CustomerTransactionRepository transactionRepo;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private PaymentTokenService paymentTokenService;


    @GetMapping
    public ResponseEntity<List<CustomerTransaction>> getAllTransaction() {
        List<CustomerTransaction> purchasesList = transactionRepo.findAll();
        return ResponseEntity.ok(purchasesList);
    }

    @GetMapping("/all-outlet-transaction")
    public ResponseEntity<List<CustomerTransaction>> getAllTransaction(
            @RequestParam(name = "outletId") Long outletId
    ) {
        List<CustomerTransaction> purchasesList = transactionService.findALlTransactionByOutlet(outletId);
        return ResponseEntity.ok(purchasesList);
    }


    @PostMapping
    public ResponseEntity<?> addNewTransaction(
            @RequestBody TransactionDto transactionDto
    ) {

        try {
            TransactionInfoDto transactionInfoDto = transactionService.addNewPurchase(transactionDto);
            return ResponseEntity.ok(transactionInfoDto);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            MessageResponse messageResponse = new MessageResponse();
            switch (e.getMessage()) {
                case "CONSTRAINT_NOT_FOUND" -> messageResponse.setMessage("Either Outlet or Supplier are not found !");
                case "NO_ITEMS" -> messageResponse.setMessage("Bill should have at least one Item !");
                case "PRODUCT_NOT_FOUND" -> messageResponse.setMessage("product not found !");
                case "ZERO_VALUE" -> messageResponse.setMessage("Quantiy can't be zero !");
                case "EXCEED_QUANTITY" -> messageResponse.setMessage("Quantity given exceed quantity on hand !");
                default -> {
                    messageResponse.setMessage("Error Occured");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
                }

            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
        }

    }


    @GetMapping("/items/{transactionId}")
    public ResponseEntity<List<CustomerProductPurchases>> getAllProductPurchases(
            @PathVariable(name = "transactionId") Long transactionId
    ) {
        List<CustomerProductPurchases> purchasesList = transactionService.findAllProductPurchasesTransaction(transactionId);
        return ResponseEntity.ok(purchasesList);
    }


    @GetMapping("/detail/{transactionId}")
    public ResponseEntity<CustomerTransaction> detailTransaction(
            @PathVariable(name = "transactionId") Long transactionId
    ) {
        try {
            CustomerTransaction transaction = transactionService.findTransactionById(transactionId);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PostMapping("/save-bills")
    public ResponseEntity<MessageResponse> saveBills(
            @RequestBody TransactionInfoDto transactionInfoDto
    ) {
        try {
            transactionService.saveTransaction(transactionInfoDto);
            return ResponseEntity.ok(new MessageResponse("Transaction Sucessfully added"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(new MessageResponse("Transaction failed to add !"));
        }
    }


    @GetMapping("/process/{transactionId}")
    public ResponseEntity<MessageResponse> processTransaction(
           @PathVariable(name = "transactionId") Long transactionId
    ) {
        try {
            transactionService.prosesTransaction(transactionId);
            return ResponseEntity.ok(new MessageResponse("Transaction Sucessfully Proceed"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(new MessageResponse("Transaction failed to proceed !"));
        }
    }

    @PostMapping("/deliver")
    public ResponseEntity<MessageResponse> deliverTransaction(
            @RequestBody DeliveryDto deliveryDto
    ) {
        try {
            transactionService.deliverTransaction(Long.parseLong(deliveryDto.getTransactionId()), deliveryDto.getDeliveryDate());
            return ResponseEntity.ok(new MessageResponse("Transaction Sucessfully Delivered"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(new MessageResponse("Transaction failed to Delivered !"));
        }
    }


    @PostMapping("/payment-details")
    public ResponseEntity<TransactionInfoDto> payTransactionPage(
           @RequestParam(name = "paymentToken") String paymentToken
    ) {

        try {
            PaymentToken payment = paymentTokenService.findByToken(paymentToken);

            CustomerTransaction customerTransaction = transactionService.findTransactionById(payment.getTransactionId());
            TransactionInfoDto transactionInfoDto = new TransactionInfoDto();
            transactionInfoDto.setTransaction(customerTransaction);
            transactionInfoDto.setPurchasesList(customerTransaction.getProductPurchasesList());
            return ResponseEntity.ok(transactionInfoDto);


        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/pay/{transactionId}")
    public ResponseEntity<MessageResponse> makePayment(
            @PathVariable(name = "transactionId") Long transactionId,
            @RequestParam(name = "token") String token
    ) {

        try {
            transactionService.makePayment(transactionId);

            PaymentToken payment = paymentTokenService.findByToken(token);
            paymentTokenService.deleteTokenById(payment.getPasswordResetTokenId());

            return ResponseEntity.ok(new MessageResponse("Payed successfully !"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setMessage("Failed to update purchase status !");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);

        }

    }




}
