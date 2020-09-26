package me.chan.spring.demo.repository;

import me.chan.spring.demo.model.Coffee;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CoffeeRepository extends MongoRepository<Coffee, String> {

    List<Coffee> findAllByName(String name);
}
