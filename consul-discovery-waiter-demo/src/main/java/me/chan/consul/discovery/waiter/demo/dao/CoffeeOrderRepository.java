package me.chan.consul.discovery.waiter.demo.dao;


import me.chan.consul.discovery.waiter.demo.model.CoffeeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Long> {
}
