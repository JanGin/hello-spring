package me.chan.resttemplate.demo;

import lombok.extern.slf4j.Slf4j;
import me.chan.resttemplate.demo.model.Coffee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@Slf4j
@SpringBootApplication
public class RestTemplateDemoApplication implements ApplicationRunner {


    @Autowired
    private RestTemplate restTemplate;


    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

//    @Autowired
//    private WebClient webClient;

//    @Bean
//    ClientHttpConnector clientHttpConnector() {
//        return new ReactorClientHttpConnector();
//    }
//
//    @Bean
//    WebClient webClient(ClientHttpConnector connector, WebClient.Builder builder) {
//        return builder.clientConnector(connector)
//                        .baseUrl("http://localhost:8080").build();
//    }

    public static void main(String[] args) {

        new SpringApplicationBuilder()
                .sources(RestTemplateDemoApplication.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8080/coffee/{id}")
                .build(1);
        ResponseEntity<Coffee> c = restTemplate.getForEntity(uri, Coffee.class);
        log.info("Response Status: {}, Response Headers: {}", c.getStatusCode(), c.getHeaders().toString());
        log.info("Coffee: {}", c.getBody());

        String coffeeUri = "http://localhost:8080/coffee/";
        Coffee request = Coffee.builder()
                .name("Americano")
                .price(BigDecimal.valueOf(40))
                .build();
        Coffee response = restTemplate.postForObject(coffeeUri, request, Coffee.class);
        log.info("New Coffee: {}", response);

        String s = restTemplate.getForObject(coffeeUri, String.class);
        log.info("String: {}", s);

//        CountDownLatch cdl = new CountDownLatch(2);
//
//        webClient.get().uri("/coffee/{id}", 1)
//                    .accept(MediaType.APPLICATION_JSON)
//                    .retrieve()
//                    .bodyToMono(Coffee.class)
//                    .doOnError(er -> log.error("error:", er))
//                    .doFinally(f -> cdl.countDown())
//                    .subscribeOn(Schedulers.single())
//                    .subscribe(coffee -> log.info("coffee 1:{}", coffee));
    }
}
