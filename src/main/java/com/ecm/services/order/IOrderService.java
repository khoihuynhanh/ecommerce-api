package com.ecm.services.order;

import com.ecm.dtos.OrderDTO;
import com.ecm.models.Order;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    Order getOrder(Long id) throws Exception;
    Order updateOrder(Long id, OrderDTO orderDTO) throws Exception;
    void deleteOrder(Long id) throws Exception;
    List<Order> findByUserId(Long userId);
}
