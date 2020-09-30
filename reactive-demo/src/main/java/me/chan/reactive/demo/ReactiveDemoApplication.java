package me.chan.reactive.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootApplication
public class ReactiveDemoApplication implements ApplicationRunner {


    private CountDownLatch cdl = new CountDownLatch(1);

    public static void main(String[] args) {
        SpringApplication.run(ReactiveDemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Flux.range(1,5).doOnRequest(i -> log.info("request num: {}", i))
                .doOnComplete(() -> log.info("complete stage 1"))
                .map(i -> {
                    log.info("Publish {}, {}", Thread.currentThread(), i);
                    // return 10 / (i - 3);    it leads to the result of subscription is negative 1
                    return i;
                })
                .doOnComplete(() -> log.info("complete stage 2"))
                .subscribeOn(Schedulers.elastic())
                .onErrorResume(ex-> {
                    log.error("Exception error:{}", ex.getMessage());
                    return Mono.just(-1);
                })
                .subscribe(e -> log.info("Subscribe:{}:{}", Thread.currentThread().getName(), e),
                        ex -> log.error("Exception catched:{}", ex.getMessage()),
                        () -> cdl.countDown());

        cdl.await();
    }
}
