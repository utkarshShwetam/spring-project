package com.vibrant.vibranium;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.vibrant.vibranium.Customer.Customer;
import com.vibrant.vibranium.Customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class vibraniumApplication {
	private final CustomerRepository customerRepository;

	public vibraniumApplication(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}


	public static void main(String[] args) {
		SpringApplication.run(vibraniumApplication.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {
			Faker faker = new Faker();
			Random random = new Random();
			Name name = faker.name();
			String fName = faker.name().firstName();
			String lName = faker.name().lastName();
			Customer customer = new Customer(
					fName+" "+lName,
                    fName.toLowerCase()+"."+lName.toLowerCase()+"@random.com",
                    random.nextInt(16,100)
			);
			customerRepository.save(customer);
		};
	}


}
