package com.vibrant.vibranium.Customer;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    private static final Random RANDOM = new Random();
    private static final String CUSTOMER_URI = "/api/v1/customers";

    @Autowired
    WebTestClient webTestClient;

    @Test
    void registerCustomer(){

        Faker FAKER = new Faker();

        // register
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID() + "@testing.com";
        Integer age = RANDOM.nextInt(1,100);
        CustomerRegisterRequest request = new CustomerRegisterRequest(
                name,email,age
        );

        // Send a request
        webTestClient.post()
                        .uri(CUSTOMER_URI)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(Mono.just(request),CustomerRegisterRequest.class)
                                .exchange()
                                .expectStatus()
                                .isOk();

        // get customer
        List<Customer> allCustomer = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        // verify customer
        Customer expectedCustomer = new Customer(name, email, age);

        assertThat(allCustomer).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        // Get customer id
        Integer id = allCustomer
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        expectedCustomer.setId(id);

        // Get Customer by ID
        webTestClient.get()
                .uri(CUSTOMER_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expectedCustomer);

    }


    @Test
    void deleteCustomer() {

        Faker FAKER = new Faker();

        // register
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID() + "@testing.com";
        Integer age = RANDOM.nextInt(1,100);
        CustomerRegisterRequest request = new CustomerRegisterRequest(
                name,email,age
        );

        // Send a request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegisterRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer
        List<Customer> allCustomer = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        // Get customer id
        Integer id = allCustomer
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // Delete customer
        webTestClient.delete()
               .uri(CUSTOMER_URI+"/{id}",id)
               .accept(MediaType.APPLICATION_JSON)
               .exchange()
               .expectStatus()
               .isOk();

        // Get Customer by ID
        webTestClient.get()
                .uri(CUSTOMER_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();

    }

    @Test
    void updateCustomer() {

        Faker FAKER = new Faker();

        // register
        String name = FAKER.name().fullName();
        String email = FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID() + "@testing.com";
        Integer age = RANDOM.nextInt(1,100);
        CustomerRegisterRequest request = new CustomerRegisterRequest(
                name,email,age
        );

        // Send a request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request),CustomerRegisterRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer
        List<Customer> allCustomer = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        // Get customer id
        Integer id = allCustomer
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // Update customers all Parameters
        String updatedName = FAKER.name().fullName();
        String updatedEmail = FAKER.internet().safeEmailAddress()+"-"+ UUID.randomUUID() + "@updateTesting.com";
        Integer updatedAge = RANDOM.nextInt(1,100);

        CustomerUpdateRequest updatedCustomerRequest = new CustomerUpdateRequest(
                updatedName,updatedEmail,updatedAge
        );

        // Update customer
        webTestClient.put()
                .uri(CUSTOMER_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedCustomerRequest),CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Get Customer by ID after updating
        Customer updatedCustomerResult = webTestClient.get()
                .uri(CUSTOMER_URI+"/{id}",id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        Customer expectedCustomer = new Customer(id,updatedName,updatedEmail,updatedAge);

        assertThat(updatedCustomerResult).isEqualTo(expectedCustomer);

    }
}
