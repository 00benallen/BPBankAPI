package com.bpinhorn.data.models;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DBCustomer {

    @Id
    public String id;

    public String name;
    
    public List<String> accounts;
    
    public String email;

    public DBCustomer() {}

}
