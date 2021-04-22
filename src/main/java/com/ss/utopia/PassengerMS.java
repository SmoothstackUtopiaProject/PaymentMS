package com.ss.utopia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class PassengerMS {

	public static void main(String[] args) {
		SpringApplication.run(PassengerMS.class, args);
	}

	@Bean
	public RestTemplate getRT() {
		return new RestTemplate();
	}
}