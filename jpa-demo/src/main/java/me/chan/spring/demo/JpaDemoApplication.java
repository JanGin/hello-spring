package me.chan.spring.demo;

import lombok.extern.slf4j.Slf4j;
import me.chan.spring.demo.model.Coffee;
import me.chan.spring.demo.repository.CoffeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
@Slf4j
public class JpaDemoApplication implements CommandLineRunner {


    @Autowired
    private CoffeeRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(JpaDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        initData();
        findCoffeeInfo();
    }


    private void initData() {
        Coffee latte = Coffee.builder().name("latte")
                        .price(new BigDecimal(30.0))
                        .build();
        repository.save(latte);
        Coffee cappuccino = Coffee.builder().name("cappuccino")
                               .price(new BigDecimal(50.0))
                                .build();
        repository.save(cappuccino);
        log.info("Coffee: {}", cappuccino);
    }

    private void findCoffeeInfo() {
        List<Coffee> list = repository.findTop5ByOrderByUpdateTimeDescIdAsc();
        list.forEach(c -> log.info(c.toString()));
    }
}
