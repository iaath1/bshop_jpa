package com.bshop_jpa.demo.repositories;



import org.springframework.data.repository.CrudRepository;

import com.bshop_jpa.demo.models.Status;

public interface StatusRepository extends CrudRepository<Status, Integer>{
    
}
