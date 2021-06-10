package me.chan.consul.discovery.waiter.demo.controller.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    private String customer;
    private List<String> items;
}
