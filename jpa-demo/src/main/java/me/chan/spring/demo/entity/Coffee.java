package me.chan.spring.demo.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by JanGin.
 */
@Entity
@Table(name = "tb_menu")
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coffee extends BaseEntity {


    private BigDecimal price;

    private String name;
}
