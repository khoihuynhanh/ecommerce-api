package com.ecm.controllers;

import com.ecm.dtos.OrderDetailDTO;
import com.ecm.models.OrderDetail;
import com.ecm.services.orderDetail.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;
    @PostMapping()
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult result
    ) throws Exception {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        OrderDetail orderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
        return ResponseEntity.ok(orderDetail);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetail> getOrderDetail(
            @Valid @PathVariable Long id
    ) throws Exception {
        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok(orderDetail);
    }

    @GetMapping("/order/{order_id}")
    public ResponseEntity<List<OrderDetail>> getAllOrderDetails(
            @Valid @PathVariable("order_id") Long orderId
    ) {
        List<OrderDetail> byOrderId = orderDetailService.findByOrderId(orderId);
        return ResponseEntity.ok(byOrderId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDetail> updateOrderDetail(
            @Valid @PathVariable Long id,
            @RequestBody OrderDetailDTO orderDetailDTO
    ) throws Exception {
        OrderDetail updateOrderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
        return ResponseEntity.ok(updateOrderDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetail(
            @Valid @PathVariable Long id
    ) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("delete " + id);
    }
}
