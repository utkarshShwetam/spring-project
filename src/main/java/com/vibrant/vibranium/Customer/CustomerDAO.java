package com.vibrant.vibranium.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDAO {

    List<Customer> getCustomer();

    Optional<Customer> getCustomerById(Integer id);

    void insertCustomer(Customer customer);

    boolean existsCustomerByEmail(String email);

    boolean existsCustomerById(Integer id);

    void deleteCustomerById(Integer id);

    void updateCustomer(Customer customerToBeUpdated);
}
