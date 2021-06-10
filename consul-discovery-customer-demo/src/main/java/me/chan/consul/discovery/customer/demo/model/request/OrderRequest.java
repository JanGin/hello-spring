package me.chan.consul.discovery.customer.demo.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class OrderRequest {

    private String customer;
    private List<String> items;
}
