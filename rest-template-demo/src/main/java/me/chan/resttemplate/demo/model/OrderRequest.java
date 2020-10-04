package me.chan.resttemplate.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    private String customer;

    private List<Coffee> items;
}
