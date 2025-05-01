package com.cpl.jumpstart.services;


import com.cpl.jumpstart.entity.Customer;
import com.cpl.jumpstart.entity.Outlet;
import com.cpl.jumpstart.entity.Supplier;
import com.cpl.jumpstart.repositories.CustomerRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OutletService outletService;

    public void addNewCustomer(Customer customer, Long outletId){

        Outlet outlet = outletService.findById(outletId);

        Long customerCode = getLastSavedCustomerId();
        if(customerCode == null) {
            customerCode = 1L;
        } else {
            customerCode += 1;
        }

        customer.setOutlet(outlet);
        customer.setCustomerCode("CUSTOMER-JP-" + customerCode);
        customer.setDateRegister(new Date(System.currentTimeMillis()));

        customerRepository.save(customer);
    }


    public Long getLastSavedCustomerId() {
        return customerRepository.findMaxId();
    }


    public List<Customer> getALlCustomer() {
        return customerRepository.findAll();
    }


    public Customer findById(Long customerId){
        return customerRepository.findById(customerId).orElseThrow(() ->
                new RuntimeException(String.format("Customer with id '%s' not found", customerId))
        );
    }

    public void updateCustomer(Long customerId, Customer updatedCustomer){

        Customer customer = findById(customerId);
        customer.setCustomerFullName(updatedCustomer.getCustomerFullName());
        customer.setAddress(updatedCustomer.getAddress());
        customer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        customer.setEmail(updatedCustomer.getEmail());

        customerRepository.save(customer);

    }

}
