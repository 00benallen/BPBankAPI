package com.bpinhorn.rest.models;

import lombok.Data;

@Data
public class DeleteCustomerRequest {
	
	private String email;
	
	private String fullName;

}
