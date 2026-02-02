package com.mycompany.service.customer.example.customer;

import com.mycompany.service.customer.CustomerApplication;
import org.springframework.boot.SpringApplication;

public class TestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.from(CustomerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
