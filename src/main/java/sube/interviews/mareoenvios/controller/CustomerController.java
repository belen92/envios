package sube.interviews.mareoenvios.controller;


import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.exception.ResourceNotFoundException;
import sube.interviews.mareoenvios.service.CustomerService;

import java.util.List;

@RestController
@RequestMapping("/customer/info")
public class CustomerController {


    @Autowired
    CustomerService customerService;

    @ApiResponse(description = "Retrieves a Customer")
    @GetMapping(value = "{id}")
    public Customer getCustomer(@PathVariable Long id) {
        Customer customer = customerService.getCustomer(id);
        if (customer == null) {
            throw new   ResourceNotFoundException("Customer with id " + id + " not found.");
        }
        return customer;
    }



    @ApiResponse(description = "Retrieves a list of all Customers")
    @GetMapping(value = "")
    public List<Customer> getCustomers() {
        List<Customer> customers = customerService.getCustomers();
        if (customers.isEmpty()) {
            throw new ResourceNotFoundException("No customers found.");
        }
        return customers;
    }
}
