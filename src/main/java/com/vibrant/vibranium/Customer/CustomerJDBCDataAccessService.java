package com.vibrant.vibranium.Customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
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
        String query = """
                SELECT count(id) FROM CUSTOMER WHERE email =?""";
       Integer result = jdbcTemplate.queryForObject(query,Integer.class,email);
       return Objects.nonNull(result) && result > 0;

    }

    @Override
    public boolean existsCustomerById(Integer id) {
        String query = """
                SELECT count(id) FROM CUSTOMER WHERE id =?""";
       Integer result = jdbcTemplate.queryForObject(query,Integer.class,id);
       return Objects.nonNull(result) && result > 0;
    }

    @Override
    public void deleteCustomerById(Integer id) {
        String query = """
                DELETE FROM CUSTOMER WHERE id =?""";
        jdbcTemplate.update(query,id);
    }

    @Override
    public void updateCustomer(Customer customerToBeUpdated) {
        if(Objects.nonNull(customerToBeUpdated.getName())){
            String query = """
                    UPDATE CUSTOMER SET name =? WHERE id =?""";
            jdbcTemplate.update(query,customerToBeUpdated.getName(),customerToBeUpdated.getId());
        }
        if(Objects.nonNull(customerToBeUpdated.getEmail())){
            String query = """
                    UPDATE CUSTOMER SET email =? WHERE id =?""";
            jdbcTemplate.update(query,customerToBeUpdated.getEmail(),customerToBeUpdated.getId());
        }
        if(Objects.nonNull(customerToBeUpdated.getAge())){
            String query = """
                    UPDATE CUSTOMER SET age =? WHERE id =?""";
            jdbcTemplate.update(query,customerToBeUpdated.getAge(),customerToBeUpdated.getId());
        }
    }
}
