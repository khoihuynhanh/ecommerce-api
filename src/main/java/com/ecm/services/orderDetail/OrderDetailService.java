package com.ecm.services.orderDetail;

import com.ecm.dtos.OrderDetailDTO;
import com.ecm.models.Order;
import com.ecm.models.OrderDetail;
import com.ecm.models.Product;
import com.ecm.repositories.OrderDetailRepository;
import com.ecm.repositories.OrderRepository;
import com.ecm.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new Exception("not found orderId"));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new Exception("not found productId"));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(existingOrder)
                .product(existingProduct)
                .price(orderDetailDTO.getPrice())
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws Exception {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new Exception("not found"));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new ExpressionException("not found"));
        if (existingOrderDetail != null) {
            Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                    .orElseThrow(() -> new Exception("not found orderId"));
            Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                    .orElseThrow(() -> new Exception("not found productId"));
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(existingOrder)
                    .product(existingProduct)
                    .price(orderDetailDTO.getPrice())
                    .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                    .totalMoney(orderDetailDTO.getTotalMoney())
                    .color(orderDetailDTO.getColor())
                    .build();
            return orderDetailRepository.save(orderDetail);
        }
        return null;
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
