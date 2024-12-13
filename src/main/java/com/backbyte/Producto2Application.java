package com.backbyte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.backbyte")
public class Producto2Application {

	public static void main(String[] args) {
		SpringApplication.run(Producto2Application.class, args);

	}

}
