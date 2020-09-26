package me.chan.spring.demo;

import lombok.extern.slf4j.Slf4j;
import me.chan.spring.demo.converter.MoneyReadConverter;
import me.chan.spring.demo.model.Coffee;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@EnableMongoRepositories
@SpringBootApplication
@Slf4j
public class MongoDemoApplication implements ApplicationRunner {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoRepository<Coffee, String> repository;

    public static void main(String[] args) {
        SpringApplication.run(MongoDemoApplication.class, args);
    }


    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(new MoneyReadConverter()));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        /**
        Coffee espresso = Coffee.builder()
                .name("espresso")
                .price(Money.of(CurrencyUnit.of("CNY"), 20.0))
                .createTime(new Date())
                .updateTime(new Date()).build();

        Coffee saved = mongoTemplate.save(espresso);
        log.info("coffee====>{}", saved);

        List<Coffee> coffees = mongoTemplate.find(Query.query(Criteria.where("name").is("espresso")), Coffee.class);
        log.info("Coffee List Size is: {}", coffees.size());
        coffees.forEach(c ->
            log.info("coffee from list:====>{}", c)
        );
         **/
        repository.deleteAll();

        Coffee espresso = Coffee.builder()
                .name("espresso")
                .price(Money.of(CurrencyUnit.of("CNY"), 20.0))
                .createTime(new Date())
                .updateTime(new Date()).build();
        Coffee latte = Coffee.builder()
                .name("latte")
                .price(Money.of(CurrencyUnit.of("CNY"), 25.0))
                .createTime(new Date())
                .updateTime(new Date()).build();

        repository.insert(Arrays.asList(espresso, latte));

        List<Coffee> list = repository.findAll(Sort.by(Sort.Order.asc("name")));
        list.forEach(c -> log.info("saved is {}", c));

        latte.setPrice(Money.of(CurrencyUnit.of("CNY"), 30.0));
        latte.setUpdateTime(new Date());
        repository.save(latte);


        Optional<Coffee> coffee = repository.findOne(Example.of(Coffee.builder().name("latte").build()));
        if (coffee.isPresent()) {
            log.info("coffee is present===>{}", coffee.get());
        }


    }
}
