package com.vibrant.vibranium.Customer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO {

    public static List<Customer> customers = new ArrayList<Customer>();
    static{
        Customer jod = new Customer(1,"JOD","jod@gmail.com",21);
        Customer op = new Customer(2,"OP","op@gmail.com",28);
        customers.add(jod);
        customers.add(op);
    }
    @Override
    public List<Customer> getCustomer() {
        return customers;
    }

    @Override
    public Optional<Customer> getCustomerById(Integer id) {
        return customers.stream().filter(customer -> customer.getId().equals(id)).findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existsCustomerByEmail(String email) {
        return customers.stream().anyMatch(customer -> customer.getEmail().equals(email));
    }

    @Override
    public boolean existsCustomerById(Integer id) {
        return customers.stream().anyMatch(customer -> customer.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customers.stream()
                .filter(customer -> customer.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer customerToBeUpdated) {
        customers.add(customerToBeUpdated);
    }


}
