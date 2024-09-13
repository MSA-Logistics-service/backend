package com.msa.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HubApplication {

	public static void main(String[] args) {
		SpringApplication.run(HubApplication.class, args);
	}

}