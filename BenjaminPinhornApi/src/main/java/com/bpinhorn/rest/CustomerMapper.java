package com.bpinhorn.rest;

import java.util.UUID;

import com.bpinhorn.data.models.DBCustomer;
import com.bpinhorn.rest.models.Customer;

public class CustomerMapper {
	
	public static Customer from(DBCustomer databaseCustomer) {
		
		return Customer.builder()
				.accounts(databaseCustomer.getAccounts())
				.email(databaseCustomer.getEmail())
				.fullName(databaseCustomer.getName())
				.build();
		
	}
	
	public static DBCustomer from(Customer customer) {
		
		return DBCustomer.builder()
				.accounts(customer.getAccounts())
				.email(customer.getEmail())
				.id(UUID.randomUUID().toString())
				.name(customer.getFullName())
				.build();
	}

}
