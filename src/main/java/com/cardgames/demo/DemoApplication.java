package com.cardgames.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.cardgames.demo.services",
		"com.cardgames.demo.repository",
		"com.cardgames.demo.shell",
		"com.cardgames.demo.statemachine",
		"com.cardgames.demo.configuration"
})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
