package me.chan.spring.demo.dao;

import me.chan.spring.demo.model.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {

    List<Coffee> findByNameInOrderById(List<String> list);

    Coffee findByName(String name);
}
