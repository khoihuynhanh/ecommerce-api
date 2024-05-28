package com.ecm.controllers;

import com.ecm.dtos.OrderDTO;
import com.ecm.models.Order;
import com.ecm.services.order.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @PostMapping()
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
    ) throws Exception {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        Order order = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<Order> getOrder(
            @Valid @PathVariable("order_id") Long orderId
    ) throws Exception {
        Order existingOrder = orderService.getOrder(orderId);
        return ResponseEntity.ok(existingOrder);
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<Order>> getOrderByUserId(
            @Valid @PathVariable("user_id") Long userId
    ) {
        List<Order> orders = orderService.findByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(
            @Valid @PathVariable Long id,
            @Valid @RequestBody OrderDTO orderDTO
    ) throws Exception {
        Order updateOrder = orderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok(updateOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(
            @Valid @PathVariable Long id
    ) throws Exception {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("delete " + id);
    }
}
