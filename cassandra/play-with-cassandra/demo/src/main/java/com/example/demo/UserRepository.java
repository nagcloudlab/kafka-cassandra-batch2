package com.example.demo;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;

public interface UserRepository extends CassandraRepository<User, String>{
//    @Query("SELECT * FROM users WHERE lastname = ?0")
//    List<User> findByLastName(String lastName);
}
