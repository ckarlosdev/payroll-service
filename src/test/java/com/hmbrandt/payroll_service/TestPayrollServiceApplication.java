package com.hmbrandt.payroll_service;

import org.springframework.boot.SpringApplication;

public class TestPayrollServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(PayrollServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
