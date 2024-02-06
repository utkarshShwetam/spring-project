package com.vibrant.vibranium.Customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("")
    public List<Customer> getCustomer() {
        return customerService.getCustomer();
    }

    @GetMapping("{id}")
    public Customer getCustomerById(@PathVariable("id") Integer id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping("")
    public void addCustomer(@RequestBody CustomerRegisterRequest customerRegisterRequest) {
        customerService.addCustomer(customerRegisterRequest);
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable("id") Integer id) {
        customerService.deleteCustomer(id);
    }

    @PutMapping("{id}")
    public void updateCustomer(@PathVariable("id") Integer id, @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        customerService.updateCustomerDetails(id, customerUpdateRequest);
    }

}
