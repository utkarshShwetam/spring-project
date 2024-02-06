package com.vibrant.vibranium.Customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO{
    private final JdbcTemplate jdbcTemplate;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Customer> getCustomer() {
        String query = """
                SELECT id,name,email,age FROM CUSTOMER""";
        return jdbcTemplate.query(query,(rs, rowNum) ->
                new Customer(rs.getInt("id"),rs.getString("name"),rs.getString("email"),rs.getInt("age"))
        );
    }

    @Override
    public Optional<Customer> getCustomerById(Integer id) {
        String query = """
                SELECT id,name,email,age FROM CUSTOMER WHERE id =?""";
        return jdbcTemplate.query(query,(rs, rowNum) ->
                new Customer(rs.getInt("id"),rs.getString("name"),rs.getString("email"),rs.getInt("age")),id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        String query = """
                INSERT INTO CUSTOMER(name,email,age) VALUES (?,?,?)""";
        jdbcTemplate.update(query,
            customer.getName(),customer.getEmail(),customer.getAge()
        );
    }

    @Override
    public boolean existsCustomerByEmail(String email) {
        return false;
    }

    @Override
    public boolean existsCustomerById(Integer id) {
        return false;
    }

    @Override
    public void deleteCustomerById(Integer id) {

    }

    @Override
    public void updateCustomer(Customer customerToBeUpdated) {

    }
}
