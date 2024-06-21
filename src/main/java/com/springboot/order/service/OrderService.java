package com.springboot.order.service;

import com.springboot.coffee.entity.Coffee;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.order.entity.Order;
import com.springboot.order.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) {
        verifyExistOrder(order);
        return orderRepository.save(order);
    };

    public Order findOrder(long orderId) {
        return findVerifiedOrder(orderId);

    }

    public Page<Order> findOrders(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.Direction.DESC, "orderId");
        Page<Order> findOrder = orderRepository.findAll(pageRequest);
        return findOrder;
    }

    public void cancelOrder(long orderId) {
        Order order = findVerifiedOrder(orderId);
        if(order.getOrderStatus().getStepNumber() > 2 ){
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_ORDER);
        }
        order.setOrderStatus(Order.OrderStatus.ORDER_CANCEL);
        orderRepository.save(order);
    }

    private Order findVerifiedOrder(long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Order findOder = optionalOrder.orElseThrow(() -> new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));
        return findOder;
    }

    private void verifyExistOrder(Order order) {
        Optional<Order> optionalOrder = orderRepository.findById(order.getOrderId());
        if(optionalOrder.isPresent()){
            throw new BusinessLogicException(ExceptionCode.ORDER_EXISTS);
        }
    }

}


