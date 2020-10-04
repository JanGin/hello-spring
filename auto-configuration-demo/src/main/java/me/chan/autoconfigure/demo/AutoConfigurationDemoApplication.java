package me.chan.autoconfigure.demo;

import geektime.spring.hello.greeting.GreetingApplicationRunner;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AutoConfigurationDemoApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(AutoConfigurationDemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

//    @Bean
//    public GreetingApplicationRunner greetingApplicationRunner() {
//        return new GreetingApplicationRunner("auto configuration demo");
//    }
}
