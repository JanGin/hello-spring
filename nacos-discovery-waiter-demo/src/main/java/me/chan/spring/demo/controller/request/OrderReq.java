package me.chan.spring.demo.controller.request;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class OrderReq {

    private String customer;
    private List<String> items;
}
