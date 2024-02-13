package com.vibrant.vibranium.Customer;

import com.vibrant.vibranium.Commons.ExceptionsHandler.ExceptionsHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService customerServiceUnderTest;

    @Mock private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {
        customerServiceUnderTest = new CustomerService(customerDAO);
    }

    @Test
    void getCustomer() {

    customerServiceUnderTest.getCustomer();

    verify(customerDAO).getCustomer();

    }

    @Test
    void getCustomerById() {

        Integer id =8;
        Customer customer = new Customer(
                id,"James Bond","james.bond@random.com",29
        );
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));

        Customer actual = customerServiceUnderTest.getCustomerById(id);

        assertThat(actual).isEqualTo(customer);

    }

    @Test
    void getCustomerByIdNotExists() {

        Integer id =8;
        when(customerDAO.getCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerServiceUnderTest.getCustomerById(id))
                .isInstanceOf(ExceptionsHandler.ResourceNotFound.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

    }

    @Test
    void addCustomer() {

        String email = "james.bond@random.com";
        CustomerRegisterRequest customer = new CustomerRegisterRequest(
                "James Bond",email,29
        );
        when(customerDAO.existsCustomerByEmail(email)).thenReturn(false);
        customerServiceUnderTest.insertCustomer(customer);

        ArgumentCaptor<Customer> c = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).insertCustomer(c.capture());
        Customer capturedCustomer = c.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.email());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.age());

    }

    @Test
    void addCustomerIfEmailAlreadyExists() {

        String email = "james.bond@random.com";
        CustomerRegisterRequest customer = new CustomerRegisterRequest(
                "James Bond",email,29
        );
        when(customerDAO.existsCustomerByEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> customerServiceUnderTest.insertCustomer(customer))
                .isInstanceOf(ExceptionsHandler.DuplicateResourceFound.class)
                .hasMessage("email already taken");

        verify(customerDAO,never()).insertCustomer(any());

    }

    @Test
    void deleteCustomer() {

        Integer customerId = 10;
        when(customerDAO.existsCustomerById(customerId)).thenReturn(true);

        customerServiceUnderTest.deleteCustomerById(customerId);

        verify(customerDAO).deleteCustomerById(customerId);

    }

    @Test
    void deleteCustomerIfNotExists() {

        Integer customerId = 10;
        when(customerDAO.existsCustomerById(customerId)).thenReturn(false);

        assertThatThrownBy(() ->customerServiceUnderTest.deleteCustomerById(customerId))
                .isInstanceOf(ExceptionsHandler.ResourceNotFound.class)
                .hasMessage("customer with id [%s] not found".formatted(customerId));

        verify(customerDAO,never()).deleteCustomerById(customerId);

    }

    @Test
    void updateCustomerAllDetails() {

        Integer id = 12;
        Customer customer = new Customer(
                id,"James Bond","james.bond@gmail.com",34
        );

        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));

        String email = "jack.heavens@gmail.com";
        CustomerUpdateRequest updateCustomer = new CustomerUpdateRequest(
                "Jack Heavens",email,22
        );

        when(customerDAO.existsCustomerByEmail(email)).thenReturn(false);

        customerServiceUnderTest.updateCustomerDetails(id,updateCustomer);

        ArgumentCaptor<Customer> c = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(c.capture());
        Customer capturedCustomer = c.getValue();

        assertThat(capturedCustomer.getEmail()).isEqualTo(updateCustomer.email());
        assertThat(capturedCustomer.getName()).isEqualTo(updateCustomer.name());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateCustomer.age());

    }

    @Test
    void updateCustomerNameDetails() {

        Integer id = 12;
        Customer customer = new Customer(
                id,"James Bond","james.bond@gmail.com",34
        );

        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));

        String name = "Jack Heavens";
        CustomerUpdateRequest updateCustomer = new CustomerUpdateRequest(
                name,null,null
        );

        customerServiceUnderTest.updateCustomerDetails(id,updateCustomer);

        ArgumentCaptor<Customer> c = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(c.capture());
        Customer capturedCustomer = c.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(updateCustomer.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void updateCustomerEmailDetails() {

        Integer id = 12;
        Customer customer = new Customer(
                id,"James Bond","james.bond@gmail.com",34
        );

        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));

        String email = "jack.heavens@gmail.com";
        CustomerUpdateRequest updateCustomer = new CustomerUpdateRequest(
                null,email,null
        );

        when(customerDAO.existsCustomerByEmail(email)).thenReturn(false);

        customerServiceUnderTest.updateCustomerDetails(id,updateCustomer);

        ArgumentCaptor<Customer> c = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(c.capture());
        Customer capturedCustomer = c.getValue();

        assertThat(capturedCustomer.getEmail()).isEqualTo(updateCustomer.email());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());

    }

    @Test
    void updateCustomerAgeDetails() {

        Integer id = 12;
        Customer customer = new Customer(
                id,"James Bond","james.bond@gmail.com",21
        );

        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));

        Integer age = 34;
        CustomerUpdateRequest updateCustomer = new CustomerUpdateRequest(
                null,null,age
        );

        customerServiceUnderTest.updateCustomerDetails(id,updateCustomer);

        ArgumentCaptor<Customer> c = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomer(c.capture());
        Customer capturedCustomer = c.getValue();

        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateCustomer.age());

    }

    @Test
    void updateCustomerIfEmailExists() {

        Integer id = 12;
        Customer customer = new Customer(
                id,"James Bond","james.bond@gmail.com",34
        );

        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));

        String email = "jack.heavens@gmail.com";
        CustomerUpdateRequest updateCustomer = new CustomerUpdateRequest(
                null,email,null
        );

        when(customerDAO.existsCustomerByEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> customerServiceUnderTest.updateCustomerDetails(id,updateCustomer))
                .isInstanceOf(ExceptionsHandler.DuplicateResourceFound.class)
                .hasMessage("email already taken");

    }

    @Test
    void updateCustomerIfNoChangesExists() {

        String email = "james.bond@gmail.com";
        Integer id = 12;
        Customer customer = new Customer(
                id,"James Bond",email,34
        );

        when(customerDAO.getCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateCustomer = new CustomerUpdateRequest(
                customer.getName(),customer.getEmail(),customer.getAge()
        );

        assertThatThrownBy(() -> customerServiceUnderTest.updateCustomerDetails(id,updateCustomer))
                .isInstanceOf(ExceptionsHandler.BadRequestFound.class)
                .hasMessage("No changes detected");

        verify(customerDAO,never()).updateCustomer(any());

    }
}