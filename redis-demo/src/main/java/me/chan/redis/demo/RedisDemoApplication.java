package me.chan.redis.demo;

import lombok.extern.slf4j.Slf4j;
import me.chan.redis.demo.domain.Coffee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Slf4j
public class RedisDemoApplication implements ApplicationRunner {


    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    private static final String HASH_KEY = "coffee:menu";

    @Bean
    ReactiveStringRedisTemplate reactiveStringRedisTemplate(ReactiveRedisConnectionFactory factory) {
        return new ReactiveStringRedisTemplate(factory);
    }

    public static void main(String[] args) {
        SpringApplication.run(RedisDemoApplication.class, args);
    }



    @Override
    public void run(ApplicationArguments args) throws Exception {
        ReactiveHashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        CountDownLatch cdl = new CountDownLatch(1);

        List<Coffee> coffees = jdbcTemplate.query("select * from t_coffee", (rs, i) ->
                Coffee.builder().id(rs.getLong("id"))
                                .name(rs.getString("name"))
                                .price(rs.getLong("price"))
                                .build());

        Flux.fromIterable(coffees)
                .publishOn(Schedulers.single())
                .doOnComplete(()->  log.info("coffee completed") )
                .flatMap(c -> {
                    log.info("ready to put coffee:{}", c);
                   return hashOps.put(HASH_KEY, c.getName(), c.getPrice().toString());
                }).doOnComplete(()-> log.info("set to redis successfully"))
                .concatWith(redisTemplate.expire(HASH_KEY, Duration.ofHours(1L)))
                .doOnComplete(()-> log.info("expired successfully"))
                .onErrorResume(c-> {
                    log.info("expire failed");
                    return Mono.just(false);
                }).subscribe(
                        (b)-> log.info("expire ok ? {}", b),
                        (e) -> log.error("exception {}", e.getMessage()),
                        () -> cdl.countDown()
                );

        cdl.await();
    }
}
