package com.example.cms_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CmsBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(CmsBackendApplication.class, args);
	}

	@GetMapping("/health")
	public String healthCheck() {
		return "OK";
	}
}
