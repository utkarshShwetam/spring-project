package com.vibrant.vibranium.Customer;

import com.vibrant.vibranium.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {

    private CustomerJDBCDataAccessService underTestCustomerJDBCDataAccessService;

    @BeforeEach
    void setUp() {
        underTestCustomerJDBCDataAccessService = new CustomerJDBCDataAccessService(getJDBCTemplate());
    }

    @Test
    void getCustomer() {

        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID(),
                24
        );
        underTestCustomerJDBCDataAccessService.insertCustomer(customer);

        List<Customer> customers = underTestCustomerJDBCDataAccessService.getCustomer();

        assertThat(customers).isNotEmpty();

    }

    @Test
    void getCustomerById() {

        String email = FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                24
        );
        underTestCustomerJDBCDataAccessService.insertCustomer(customer);

        Integer id = underTestCustomerJDBCDataAccessService.getCustomer()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> actualCustomer = underTestCustomerJDBCDataAccessService.getCustomerById(id);

        assertThat(actualCustomer).isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c.getEmail()).isEqualTo(email);
                    assertThat(c.getName()).isEqualTo(customer.getName());
                    assertThat(c.getId()).isEqualTo(id);
                    assertThat(c.getAge()).isEqualTo(customer.getAge());
                });

    }

    @Test
    void getCustomerByIdNotExists() {

        Integer id =-1;

        Optional<Customer> actualCustomer = underTestCustomerJDBCDataAccessService.getCustomerById(id);

        assertThat(actualCustomer).isEmpty();

    }

    @Test
    void insertCustomer() {

        String email = FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                24
        );

        underTestCustomerJDBCDataAccessService.insertCustomer(customer);

        Integer id = underTestCustomerJDBCDataAccessService.getCustomer()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> actualCustomer = underTestCustomerJDBCDataAccessService.getCustomerById(id);

        assertThat(actualCustomer).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void existsCustomerByEmail() {

        String customerEmail = FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                customerEmail,
                24
        );

        underTestCustomerJDBCDataAccessService.insertCustomer(customer);

        String email = underTestCustomerJDBCDataAccessService.getCustomer()
                .stream()
                .map(Customer::getEmail)
                .filter(cEmail -> cEmail.equals(customerEmail))
                .findFirst()
                .orElseThrow();

        boolean result = underTestCustomerJDBCDataAccessService.existsCustomerByEmail(email);

        assertThat(result).isTrue();

    }

    @Test
    void notExistsCustomerByEmail() {
        String email = FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID();

        boolean result = underTestCustomerJDBCDataAccessService.existsCustomerByEmail(email);

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

        underTestCustomerJDBCDataAccessService.insertCustomer(customer);

        Integer id = underTestCustomerJDBCDataAccessService.getCustomer()
                .stream()
                .filter(cEmail -> cEmail.getEmail().equals(customerEmail))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        boolean result = underTestCustomerJDBCDataAccessService.existsCustomerById(id);

        assertThat(result).isTrue();

    }

    @Test
    void notExistsCustomerById() {

        Integer id =-1;

        boolean result = underTestCustomerJDBCDataAccessService.existsCustomerById(id);

        assertThat(result).isFalse();

    }

    @Test
    void deleteCustomerById() {

        String customerEmail = FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                customerEmail,
                24
        );

        underTestCustomerJDBCDataAccessService.insertCustomer(customer);

        Integer id = underTestCustomerJDBCDataAccessService.getCustomer()
                .stream()
                .filter(cEmail -> cEmail.getEmail().equals(customerEmail))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        underTestCustomerJDBCDataAccessService.deleteCustomerById(id);

        Optional<Customer> actualDeletedCustomer = underTestCustomerJDBCDataAccessService.getCustomerById(id);

        assertThat(actualDeletedCustomer).isNotPresent();

    }

    @Test
    void updateAllFieldsOfCustomer() {

        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID(),
                24
        );

        underTestCustomerJDBCDataAccessService.insertCustomer(customer);

        Integer id = underTestCustomerJDBCDataAccessService.getCustomer()
                .stream()
                .filter(cEmail -> cEmail.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        String newName = FAKER.name().fullName();
        String newEmail = FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Integer newAge = 33;

        Customer request = new Customer();
        request.setId(id);
        request.setName(newName);
        request.setEmail(newEmail);
        request.setAge(newAge);

        underTestCustomerJDBCDataAccessService.updateCustomer(request);

        Optional<Customer> actualUpdatedCustomer = underTestCustomerJDBCDataAccessService.getCustomerById(id);

        assertThat(actualUpdatedCustomer).isPresent().hasValue(request);

    }

    @Test
    void updateCustomerEmail() {

        String email = FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                24
        );

        underTestCustomerJDBCDataAccessService.insertCustomer(customer);

        Integer id = underTestCustomerJDBCDataAccessService.getCustomer()
                .stream()
                .filter(cEmail -> cEmail.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        String newEmail = FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID();

        Customer request = new Customer();
        request.setId(id);
        request.setEmail(newEmail);

        underTestCustomerJDBCDataAccessService.updateCustomer(request);

        Optional<Customer> actualUpdatedCustomer = underTestCustomerJDBCDataAccessService.getCustomerById(id);

        assertThat(actualUpdatedCustomer).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getEmail()).isEqualTo(newEmail);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void updateCustomerName() {

        String name = FAKER.name().fullName();
        Customer customer = new Customer(
                name,
                FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID(),
                24
        );

        underTestCustomerJDBCDataAccessService.insertCustomer(customer);

        Integer id = underTestCustomerJDBCDataAccessService.getCustomer()
                .stream()
                .filter(c -> c.getName().equals(name))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        String newName = FAKER.name().fullName();

        Customer request = new Customer();
        request.setId(id);
        request.setName(newName);

        underTestCustomerJDBCDataAccessService.updateCustomer(request);

        Optional<Customer> actualUpdatedCustomer = underTestCustomerJDBCDataAccessService.getCustomerById(id);

        assertThat(actualUpdatedCustomer).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void updateCustomerAge() {

        String email = FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                24
        );

        underTestCustomerJDBCDataAccessService.insertCustomer(customer);

        int id = underTestCustomerJDBCDataAccessService.getCustomer()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Integer newAge = 33;

        Customer request = new Customer();
        request.setId(id);
        request.setAge(newAge);

        underTestCustomerJDBCDataAccessService.updateCustomer(request);

        Optional<Customer> actualUpdatedCustomer = underTestCustomerJDBCDataAccessService.getCustomerById(id);

        assertThat(actualUpdatedCustomer).isPresent().hasValueSatisfying(c ->{
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(newAge);
        });

    }
}