package com.bpinhorn.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class BenPinhornRestController {
	
	@GetMapping("/api/data")
	public DataModel getData() {
		
		return new DataModel("real data");
		
	}

}
