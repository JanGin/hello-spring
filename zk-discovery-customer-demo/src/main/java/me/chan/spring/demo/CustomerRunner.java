package me.chan.spring.demo;

import lombok.extern.slf4j.Slf4j;
import me.chan.spring.demo.model.Coffee;
import me.chan.spring.demo.model.CoffeeOrder;
import me.chan.spring.demo.model.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class CustomerRunner implements ApplicationRunner {


    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DiscoveryClient discoveryClient;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        showServiceInstances();
        readMenu();
        Long id = orderCoffee();
        queryOrder(id);
    }


    public void showServiceInstances() {
        discoveryClient.getInstances("waiter-service")
                .forEach(service -> {
                    log.info("id:{}, host:{}, port:{}",
                            service.getInstanceId(),
                            service.getHost(),
                            service.getPort());
                });
    }

    public void readMenu() {
        ParameterizedTypeReference<List<Coffee>> ptr =
                new ParameterizedTypeReference<List<Coffee>>() {};
        ResponseEntity<List<Coffee>> list = restTemplate
                .exchange("http://waiter-service/coffee/", HttpMethod.GET, null, ptr);
        list.getBody().forEach(c -> log.info("Coffee: {}", c));
    }


    public Long orderCoffee() {
            OrderRequest orderRequest = OrderRequest.builder()
                    .customer("Li Lei")
                    .items(Arrays.asList("capuccino"))
                    .build();
            RequestEntity<OrderRequest> request = RequestEntity
                    .post(UriComponentsBuilder.fromUriString("http://waiter-service/order/").build().toUri())
                    .body(orderRequest);
            ResponseEntity<CoffeeOrder> response = restTemplate.exchange(request, CoffeeOrder.class);
            log.info("Order Request Status Code: {}", response.getStatusCode());
            Long id = response.getBody().getId();
            log.info("Order ID: {}", id);
            return id;
    }


    public CoffeeOrder queryOrder(Long id) {
        CoffeeOrder order = restTemplate.getForObject("http://waiter-service/order/{id}", CoffeeOrder.class, id);
        log.info("query order returned:{}", order);
        return order;
    }
}
