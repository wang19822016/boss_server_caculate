package com.seastar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReportCalculateDataApplication
{

	public static void main(String[] args) {
		SpringApplication.run(ReportCalculateDataApplication.class, args);
	}
}
