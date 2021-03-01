package me.chan.spring.demo;

import lombok.extern.slf4j.Slf4j;
import me.chan.spring.demo.model.Coffee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@Slf4j
@EnableWebFlux
public class WebclientDemoApplication implements ApplicationRunner {


    @Autowired
    private WebClient webClient;

    @Bean
    WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8080").build();
    }


    public static void main(String[] args) {
        SpringApplication.run(WebclientDemoApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        CountDownLatch cdl = new CountDownLatch(2);

        webClient.get().uri("/coffee/{id}", 1)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Coffee.class)
                    .doOnError(er -> log.error("error:", er))
                    .doFinally(f -> cdl.countDown())
                    .subscribeOn(Schedulers.single())
                    .subscribe(coffee -> log.info("coffee 1:{}", coffee));
    }
}
