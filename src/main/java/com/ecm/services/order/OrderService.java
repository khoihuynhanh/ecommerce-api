package com.ecm.services.order;

import com.ecm.dtos.OrderDTO;
import com.ecm.models.Order;
import com.ecm.models.OrderStatus;
import com.ecm.models.User;
import com.ecm.repositories.OrderRepository;
import com.ecm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    
    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new Exception("not found"));

        LocalDate shippingDate = orderDTO.getShippingDate() != null
                ? orderDTO.getShippingDate()
                : LocalDate.now();

        if (shippingDate.isBefore(LocalDate.now())) {
            throw new Exception("shipping date wrong");
        }

        Order order = Order.builder()
                .user(existingUser)
                .fullName(orderDTO.getFullName())
                .email(orderDTO.getEmail())
                .phoneNumber(orderDTO.getPhoneNumber())
                .address(orderDTO.getAddress())
                .note(orderDTO.getNote())
                .orderDate(LocalDate.now())
                .status(OrderStatus.PENDING)
                .totalMoney(orderDTO.getTotalMoney())
                .shippingMethod(orderDTO.getShippingMethod())
                .shippingAddress(orderDTO.getShippingAddress())
                .shippingDate(shippingDate)
                .trackingNumber(orderDTO.getTrackingNumber())
                .paymentMethod(orderDTO.getPaymentMethod())
                .active(true)
                .build();
        return orderRepository.save(order);
    }

    @Override
    public Order getOrder(Long id) throws Exception {
        return orderRepository.findById(id)
                .orElseThrow(() -> new Exception("not found id"));
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) throws Exception {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new Exception("not found id"));

        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new Exception("not found user id"));

        LocalDate shippingDate = orderDTO.getShippingDate() != null
                ? orderDTO.getShippingDate()
                : LocalDate.now();

        if (shippingDate.isBefore(LocalDate.now())) {
            throw new Exception("shipping date wrong");
        }

        Order updateOrder = Order.builder()
                .user(existingUser)
                .fullName(orderDTO.getFullName())
                .email(orderDTO.getEmail())
                .phoneNumber(orderDTO.getPhoneNumber())
                .address(orderDTO.getAddress())
                .note(orderDTO.getNote())
                .orderDate(existingOrder.getOrderDate())
                .status(OrderStatus.PENDING)
                .totalMoney(orderDTO.getTotalMoney())
                .shippingMethod(orderDTO.getShippingMethod())
                .shippingAddress(orderDTO.getShippingAddress())
                .shippingDate(shippingDate)
                .trackingNumber(existingOrder.getTrackingNumber())
                .paymentMethod(orderDTO.getPaymentMethod())
                .active(existingOrder.getActive())
                .build();
        return orderRepository.save(updateOrder);
    }

    @Override
    public void deleteOrder(Long id) throws Exception {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new Exception("not found id"));
        if (order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
