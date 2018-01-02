package com.bpinhorn.rest;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.stereotype.Component;

@Component
public class MessagingUtil<T> {
	
	public String buildViolationMessage(Set<ConstraintViolation<T>> violations) {
		
		StringBuilder messageBuilder = new StringBuilder();
		
		for(ConstraintViolation<?> violation : violations) {
			messageBuilder.append(String.format("Violation on Object %s, {Property: [%s] Violation: [%s]},",
					violation.getRootBean().toString(),
					violation.getPropertyPath() != null
						? violation.getPropertyPath().toString()
						: "N/A",
					violation.getMessage()));
		}
		
		return messageBuilder.toString();
		
	}

}
