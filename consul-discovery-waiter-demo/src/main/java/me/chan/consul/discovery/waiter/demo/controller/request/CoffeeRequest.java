package me.chan.consul.discovery.waiter.demo.controller.request;

import lombok.Data;
import org.joda.money.Money;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CoffeeRequest {

    @NotEmpty
    private String name;
    @NotNull
    private Money price;
}
