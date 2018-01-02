package com.bpinhorn.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bpinhorn.data.CustomerRepository;
import com.bpinhorn.data.models.DBCustomer;
import com.bpinhorn.rest.exceptions.CustomerNotUniqueException;
import com.bpinhorn.rest.models.Customer;
import com.bpinhorn.rest.models.DeleteCustomerRequest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@AllArgsConstructor
@Slf4j
public class CustomerController {
	
	private CustomerRepository mongoRepo;
	
	private Validator validator;
	
	private MessagingUtil<Customer> util;
	
	/**
	 * For testing
	 * @return
	 */
	@GetMapping("api/data")
	public DataModel getData() {
		
		return new DataModel("real data");
		
	}
	
	@PutMapping("api/customers")
	public ResponseEntity<Void> setCustomers(@RequestBody List<Customer> customers) {
		
		/*
		 * Spring Boot cannot validate List<T> with @Valid on @RequestMapping functions, we will do it manually
		 */
		Set<ConstraintViolation<Customer>> violations = new HashSet<ConstraintViolation<Customer>>();
		for(Customer customer : customers) {
			violations.addAll(validator.validate(customer));
		}
		
		if(!violations.isEmpty()) {
			throw new ConstraintViolationException(
					util.buildViolationMessage(violations), 
					violations);
		}
		
		List<DBCustomer> customersToSave = customers.stream()
				.map(e -> CustomerMapper.from(e))
				.collect(Collectors.toList());
		
		try {
			mongoRepo.deleteAll();
			mongoRepo.save(customersToSave);
			
			return new ResponseEntity<Void>(HttpStatus.OK);
			
		} catch (DataAccessException e) {
			
			log.info("Exception encountered setting customer collection:", e);
			throw e;
			
		}
	}
	
	
	@GetMapping("api/customers")
	public ResponseEntity<List<Customer>> getCustomers() {
		
		try {
			
			List<DBCustomer> dbCustomers = mongoRepo.findAll();
			
			List<Customer> customersToReturn = dbCustomers.stream()
					.map(e -> CustomerMapper.from(e))
					.collect(Collectors.toList());
			
			return new ResponseEntity<List<Customer>>(customersToReturn, HttpStatus.OK);
			
		} catch(DataAccessException e) {
			log.info("Exception encountered setting customer collection:", e);
			throw e;
		}
	}
	
	@PostMapping("api/customers")
	public ResponseEntity<String> addCustomer(@RequestBody @Valid Customer newCustomer) {
		
		try {
			
			if(mongoRepo.findByName(newCustomer.getFullName()) != null) {
				throw new CustomerNotUniqueException(String.format("Another Customer exists with fullName: %s", newCustomer.getFullName()));
			} else if(mongoRepo.findByEmail(newCustomer.getEmail()) != null) {
				throw new CustomerNotUniqueException(String.format("Another Customer exists with email: %s", newCustomer.getEmail()));
			}
			
			mongoRepo.save(CustomerMapper.from(newCustomer));
		} catch (DataAccessException e) {
			log.info("Exception encountered adding customer to collection.", e);
			throw e;
		}
		
		return new ResponseEntity<String>("Customer added to customer database.", HttpStatus.OK);
		
	}
	
	@DeleteMapping("api/customers")
	public ResponseEntity<String> deleteCustomer(@RequestBody DeleteCustomerRequest request) {
		
		String message = null;
		try {
			DBCustomer customer = mongoRepo.findByEmail(request.getEmail());
			
			if(customer == null) {
				customer = mongoRepo.findByName(request.getFullName());
				message = "Customer could not be found by email, but was found by name. Validate email was correct.";
			}
			
			mongoRepo.delete(customer);
		} catch (DataAccessException e) {
			log.info("Exception encountered deleting customer from collection.");
			throw e;
		} catch (IllegalArgumentException e) {
			log.info("Customer does not exist in collection to delete.");
			throw e;
		}
		
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
}
