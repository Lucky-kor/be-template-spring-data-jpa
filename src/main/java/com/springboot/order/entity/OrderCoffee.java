package com.springboot.order.entity;


import com.springboot.coffee.entity.Coffee;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

//Order Coffee N:M 조인테이블
//Order OderCoffees를 manyToOne JoinColumn(ORDER_ID)
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ORDER_COFFEE")
public class OrderCoffee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderCoffeeId;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
//order -> 외래키이고 그안에 참조해야하는 기본키가 위에 있는 컬럼명
    private Order order;

    public void addOrder(Order order){
        if(!order.getOrderCoffees().contains(this)){
            order.addOrderCoffee(this);
        }
        this.order = order;
    }

    @ManyToOne
    @JoinColumn(name = "COFFEE_ID")
    private Coffee coffee;


}
