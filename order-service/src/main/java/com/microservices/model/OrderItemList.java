package com.microservices.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "t-order-item-list")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
