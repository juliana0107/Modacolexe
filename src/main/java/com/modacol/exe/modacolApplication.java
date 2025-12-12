package com.modacol.exe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.modacol.exe.")
public class modacolApplication {

	public static void main(String[] args) {

        SpringApplication.run(modacolApplication.class, args);
	}
}
