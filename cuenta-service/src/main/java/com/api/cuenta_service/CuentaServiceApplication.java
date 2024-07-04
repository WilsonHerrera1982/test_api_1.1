package com.api.cuenta_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.api.cuenta_service"})
public class CuentaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuentaServiceApplication.class, args);
	}

}
