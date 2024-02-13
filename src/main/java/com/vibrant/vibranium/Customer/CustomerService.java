package com.vibrant.vibranium.Customer;

import com.vibrant.vibranium.Commons.ExceptionsHandler.ExceptionsHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getCustomer() {
        return customerDAO.getCustomer();
    }

    public Customer getCustomerById(Integer id) {
        return customerDAO.getCustomerById(id).orElseThrow(() -> new ExceptionsHandler.ResourceNotFound("customer with id [%s] not found".formatted(id)));
    }

    public void insertCustomer(CustomerRegisterRequest customerRegisterRequest) {
        // If email exists throw exception
        if(customerDAO.existsCustomerByEmail(customerRegisterRequest.email()))
            throw new ExceptionsHandler.DuplicateResourceFound("email already taken");

        if(Objects.isNull(customerRegisterRequest.age()))
            throw new ExceptionsHandler.BadRequestFound("Age is Mandatory");
        if(Objects.isNull(customerRegisterRequest.email()))
            throw new ExceptionsHandler.BadRequestFound("Email is Mandatory");
        if(Objects.isNull(customerRegisterRequest.name()))
            throw new ExceptionsHandler.BadRequestFound("Name is Mandatory");

        // Add customer requested
        customerDAO.insertCustomer(new Customer(customerRegisterRequest.name(), customerRegisterRequest.email(), customerRegisterRequest.age()));
    }

    public void deleteCustomerById(Integer id) {
        if(!customerDAO.existsCustomerById(id))
            throw new ExceptionsHandler.ResourceNotFound("customer with id [%s] not found".formatted(id));
        // Delete customer requested
        customerDAO.deleteCustomerById(id);
    }

    public void updateCustomerDetails(Integer id, CustomerUpdateRequest customerUpdateRequest) {
        // Get customer requested
        Customer customerToBeUpdated = getCustomerById(id);
        boolean changes = false;

        // Update customer requested
        if(Objects.nonNull(customerUpdateRequest.age()) && !customerToBeUpdated.getAge().equals(customerUpdateRequest.age())) {
            customerToBeUpdated.setAge(customerUpdateRequest.age());
            changes = true;
        }
        if(Objects.nonNull(customerUpdateRequest.email()) && !customerToBeUpdated.getEmail().equals(customerUpdateRequest.email())) {
            if(customerDAO.existsCustomerByEmail(customerUpdateRequest.email()))
                throw new ExceptionsHandler.DuplicateResourceFound("email already taken");
            customerToBeUpdated.setEmail(customerUpdateRequest.email());
            changes = true;
        }
        if(Objects.nonNull(customerUpdateRequest.name()) && !customerToBeUpdated.getName().equals(customerUpdateRequest.name())) {
            customerToBeUpdated.setName(customerUpdateRequest.name());
            changes = true;
        }

        if(!changes)
            throw new ExceptionsHandler.BadRequestFound("No changes detected");

        // Save Updated customer
        customerDAO.updateCustomer(customerToBeUpdated);

    }
}
