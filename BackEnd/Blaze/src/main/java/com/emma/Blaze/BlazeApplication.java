package com.emma.Blaze;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.emma.Blaze")
public class BlazeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlazeApplication.class, args);
	}

}
