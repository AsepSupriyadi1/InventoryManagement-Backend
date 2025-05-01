package com.cpl.jumpstart.controller;


import com.cpl.jumpstart.dto.request.CustomerDto;
import com.cpl.jumpstart.dto.response.MessageResponse;
import com.cpl.jumpstart.entity.Customer;
import com.cpl.jumpstart.repositories.CustomerRepository;
import com.cpl.jumpstart.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;


    @PostMapping
    public ResponseEntity<MessageResponse> addNewCustomer(@RequestBody CustomerDto reqCustomer){

        Customer customer = new Customer();
        customer.setCustomerFullName(reqCustomer.getCustomerFullName());
        customer.setEmail(reqCustomer.getEmail());
        customer.setPhoneNumber(reqCustomer.getPhoneNumber());
        customer.setAddress(reqCustomer.getAddress());

        try {
            customerService.addNewCustomer(customer, Long.parseLong(reqCustomer.getOutletId()));
            return ResponseEntity.ok(new MessageResponse("Customer added successfully !"));
        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Null Column Identified"));
        }

    }


    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomer(){
        List<Customer> customers = customerService.getALlCustomer();
        return ResponseEntity.ok((customers));
    }

    @GetMapping("/detail/{customerId}")
    public ResponseEntity<?> getAllCustomer(
            @PathVariable(name = "customerId") Long customerId
    ){

        try {
            Customer customerDetails = customerService.findById(customerId);
            return ResponseEntity.ok(customerDetails);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Not Found"));
        }
    }


    @PutMapping("/update/{customerId}")
    public ResponseEntity<MessageResponse> customerDetail(
            @PathVariable(name = "customerId") Long customerId,
            @RequestBody Customer customer
    ){

        try {
            customerService.updateCustomer(customerId, customer);
            return ResponseEntity.ok(new MessageResponse("Customer updated successfully"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<MessageResponse> deleteCustomer(
            @PathVariable(name = "customerId") Long customerId
    ){

        try {

            customerRepository.deleteById(customerId);
            return ResponseEntity.ok(new MessageResponse("Delete Customer Success for id" + customerId));

        } catch (RuntimeException e){

            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Delete Customer failed for id " + customerId));

        }

    }


}
