package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyController {
	@RequestMapping("/")
	public String hello(){
		return "Hey, Spring Boot 的 Hello World ! ";
	}
}
