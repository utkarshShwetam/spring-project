package com.vibrant.vibranium.Customer;

import com.vibrant.vibranium.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainers {

    @Autowired
    private CustomerRepository underTestCustomerRepository;

    @BeforeEach
    void setUp() {
        underTestCustomerRepository.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {

        String customerEmail = FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                customerEmail,
                24
        );

        underTestCustomerRepository.save(customer);

        String email = underTestCustomerRepository.findAll()
                .stream()
                .map(Customer::getEmail)
                .filter(cEmail -> cEmail.equals(customerEmail))
                .findFirst()
                .orElseThrow();

        boolean result = underTestCustomerRepository.existsCustomerByEmail(email);

        assertThat(result).isTrue();

    }

    @Test
    void notExistsCustomerByEmail() {
        String email = FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID();

        boolean result = underTestCustomerRepository.existsCustomerByEmail(email);

        assertThat(result).isFalse();
    }

    @Test
    void existsCustomerById() {

        String customerEmail = FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                customerEmail,
                24
        );

        underTestCustomerRepository.save(customer);

        Integer id = underTestCustomerRepository.findAll()
                .stream()
                .filter(cEmail -> cEmail.getEmail().equals(customerEmail))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        boolean result = underTestCustomerRepository.existsCustomerById(id);

        assertThat(result).isTrue();

    }

    @Test
    void notExistsCustomerById() {

        Integer id =-1;

        boolean result = underTestCustomerRepository.existsCustomerById(id);

        assertThat(result).isFalse();

    }

}