package com.bpinhorn.rest.models;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
	
	@Email
	private String email;
	
	@NotNull
	private List<String> accounts;
	
	@NotBlank
	private String fullName;

}
