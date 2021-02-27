package com.example.crudapi.repository;

import com.example.crudapi.model.Address;
import com.example.crudapi.model.Students;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Students, String> {
    @Query("{'location.city':?0}")
    List<Students> findByCity(String city);
}
