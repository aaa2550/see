package com.yhl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="com.yhl.see.console")
public class SeeConsoleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeeConsoleApplication.class, args);
	}
}
