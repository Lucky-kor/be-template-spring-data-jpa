package com.springboot.order.entity;

import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDER_REQUEST;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, name = "LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    public void addMember(Member member){
        if(!member.getOrders().contains(this)){
            member.addOrder(this);
        }
        this.member = member;
    }

//    1:N 에서는 onetomany mappedBy - 나를 참고하고있는 객체의 변수명;
//    1인쪽이기때문에 다에 해당하는 객체를 리스트로 add메서드로 추가
    @OneToMany(mappedBy = "order")
    private List<OrderCoffee> orderCoffees = new ArrayList();
    public void addOrderCoffee(OrderCoffee orderCoffee){
        orderCoffees.add(orderCoffee);
        if(orderCoffee.getOrder() != this){
            orderCoffee.addOrder(this);
        }
    }


    public enum OrderStatus {
        ORDER_REQUEST(1, "주문 요청"),
        ORDER_CONFIRM(2, "주문 확정"),
        ORDER_COMPLETE(3, "주문 완료"),
        ORDER_CANCEL(4, "주문 취소");

        @Getter
        private int stepNumber;

        @Getter
        private String stepDescription;

        OrderStatus(int stepNumber, String stepDescription) {
            this.stepNumber = stepNumber;
            this.stepDescription = stepDescription;
        }
    }
}
