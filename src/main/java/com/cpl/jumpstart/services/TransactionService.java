package com.cpl.jumpstart.services;

import com.cpl.jumpstart.dto.request.ProductPurchasesDto;
import com.cpl.jumpstart.dto.request.PurchaseDto;
import com.cpl.jumpstart.dto.request.TransactionDto;
import com.cpl.jumpstart.dto.request.TransactionProductDto;
import com.cpl.jumpstart.dto.response.BillsInfoDto;
import com.cpl.jumpstart.dto.response.TransactionInfoDto;
import com.cpl.jumpstart.entity.*;
import com.cpl.jumpstart.entity.constraint.PurchasesStatus;
import com.cpl.jumpstart.entity.constraint.TransactionStatus;
import com.cpl.jumpstart.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    public CustomerTransactionRepository transactionRepo;

    @Autowired
    public CustomerProductPurchaseRepository transactionProductRepo;

    @Autowired
    public StockProductRepository stockProductRepo;

    @Autowired
    public ProductServices productServices;

    @Autowired
    private OutletService outletService;

    @Autowired
    private StockProductService stockProductService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OutletRepository outletRepository;

    @Autowired
    private UserAppServices userAppServices;

    @Autowired
    private PaymentTokenService paymentTokenService;

    @Autowired
    private EmailSenderService emailSenderService;


    public TransactionInfoDto addNewPurchase(
            TransactionDto transactionDto
    ) {

        CustomerTransaction transaction = new CustomerTransaction();
        UserApp staff = userAppServices.getCurrentUser();
        Outlet outlet;

        try {
            outlet = outletService.findById(Long.parseLong(transactionDto.getOutletId()));
            transaction.setOutlet(outlet);

            Customer customer = customerService.findById(Long.parseLong(transactionDto.getCustomerId()));
            transaction.setCustomerCode(customer.getCustomerCode());
            transaction.setCustomerName(customer.getCustomerFullName());
            transaction.setCustomerPhoneNumber(customer.getPhoneNumber());
            transaction.setCustomerEmail(customer.getEmail());

        } catch (Exception e) {
            throw new RuntimeException("CONSTRAINT_NOT_FOUND");
        }


        if(transactionDto.getProductDtoList().size() == 0){
            throw new RuntimeException("NO_ITEMS");
        }


        List<CustomerProductPurchases> purchasedProductList = new ArrayList<>();
        double totalAmount = 0;

        for (TransactionProductDto requestProduct : transactionDto.getProductDtoList()) {

            Product product;

            try {
                product = productServices.findProductById(requestProduct.getProductId());
            } catch (Exception e){
                throw new RuntimeException("PRODUCT_NOT_FOUND");
            }

            CustomerProductPurchases productPurchases = new CustomerProductPurchases();
            productPurchases.setProductName(product.getProductName());
            productPurchases.setProductId(product.getProductId().toString());
            productPurchases.setTransaction(transaction);

            StockProduct productRules = stockProductService.findByProductAndOutlet(product.getProductId(), outlet.getOutletId());

            if(requestProduct.getQuantity() == 0){
                throw new RuntimeException("ZERO_VALUE");
            }

            if(productRules != null){
                boolean isExceedQuantity = requestProduct.getQuantity() > productRules.getCurrentQuantity();
                if(isExceedQuantity) {
                    throw new RuntimeException("EXCEED_QUANTITY");
                }
            }


            productPurchases.setQuantity(requestProduct.getQuantity());
            purchasedProductList.add(productPurchases);

            totalAmount += product.getPrices() * requestProduct.getQuantity();
        }

        // DATE
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        transaction.setDateTime(dateFormat.format(currentDate));


        // USER DETAILS
        transaction.setStaffName(staff.getFullName());
        transaction.setStaffCode(staff.getStaffCode());

        // PURCHASES DETAIL
        transaction.setTotalAmount(totalAmount);
        transaction.setTransactionStatus(TransactionStatus.PENDING);

        Long transactionCode = getLastSavedTransactionId();
        if(transactionCode == null) {
            transactionCode = 1L;
        } else {
            transactionCode += 1;
        }

        transaction.setTransactionCode("SALES-JP-" + transactionCode);

        return new TransactionInfoDto(transaction, purchasedProductList);
    }


    public void saveTransaction(TransactionInfoDto transactionInfoDto){
        transactionRepo.save(transactionInfoDto.getTransaction());

        for(CustomerProductPurchases customerProductPurchases : transactionInfoDto.getPurchasesList()){
            CustomerProductPurchases items = new CustomerProductPurchases();
            items.setQuantity(customerProductPurchases.getQuantity());
            items.setTransaction(transactionInfoDto.getTransaction());
            items.setProductId(customerProductPurchases.getProductId());
            items.setProductName(customerProductPurchases.getProductName());
            transactionProductRepo.save(items);
        }
    }

    public void prosesTransaction(Long transactionId){
        CustomerTransaction transaction = findTransactionById(transactionId);
        transaction.setTransactionStatus(TransactionStatus.PROCESS);
        transactionRepo.save(transaction);


        String toEmail = "asepsupyad789@gmail.com";
        String subjectEmail = "Delivery Product - Jumpstart";
        String bodyEmail = "Your Transaction is proceed by outlet";
        emailSenderService.sendSimpleEmail(toEmail, bodyEmail, subjectEmail);
    }

    public void deliverTransaction(Long transactionId, Date deliveryDate){
        CustomerTransaction transaction = findTransactionById(transactionId);
        transaction.setTransactionStatus(TransactionStatus.DELIVER);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        transaction.setDeliverStartDate(format.format(deliveryDate));
        transactionRepo.save(transaction);


        PaymentToken paymentToken = new PaymentToken();
        paymentToken.setTransactionId(transaction.getTransactionId());
        paymentToken.setToken(UUID.randomUUID().toString());
        paymentTokenService.saveToken(paymentToken);

        String frontEndURL = "http://localhost:5173"; // front end URL
        String fullUrl = frontEndURL + "/payment?token=" + paymentToken.getToken();

        String toEmail = "asepsupyad789@gmail.com";
        String subjectEmail = "Delivery Product - Jumpstart";
        String bodyEmail = "Your transaction is on delivery, If your product has arrived, please make a payment to complete the processes by going to this URL below \n " + fullUrl;

        emailSenderService.sendSimpleEmail(toEmail, bodyEmail, subjectEmail);
    }



    public void makePayment(Long transactionId){
        CustomerTransaction transaction = findTransactionById(transactionId);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        transaction.setReceiveDate(format.format(new Date()));
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);

        Outlet outlet = transaction.getOutlet();
        double totalRevenue = outlet.getTotalRevenue() + transaction.getTotalAmount();
        outlet.setTotalRevenue(totalRevenue);
        outletRepository.save(outlet);

        transactionRepo.save(transaction);
    }


    public CustomerTransaction findTransactionById(Long transactionId){
        return transactionRepo.findById(transactionId).orElseThrow(
                () -> new RuntimeException(String.format("Transaction not found for id %s", transactionId))
        );
    }


    public List<CustomerTransaction> findALlTransactionByOutlet(Long outletId){
        return transactionRepo.findAllTransactionByOutlet(outletId);
    }


    public List<CustomerProductPurchases> findAllProductPurchasesTransaction(Long transactionId){
        return transactionProductRepo.findAllByProductByTransactionId(transactionId);
    }


    public Long getLastSavedTransactionId() {
        return transactionRepo.findMaxId();
    }



}
