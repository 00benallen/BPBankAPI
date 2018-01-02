package com.bpinhorn.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bpinhorn.data.models.DBCustomer;

@Repository
public interface CustomerRepository extends MongoRepository<DBCustomer, String> {

    public DBCustomer findByName(String name);
    public DBCustomer findByEmail(String email);
    public void deleteByEmail(String email);
    public void deleteByName(String name);

}
