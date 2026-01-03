package com.tstamelos.intelligentLogisticsAPI.controller;

import com.tstamelos.intelligentLogisticsAPI.record.OrderParams;
import com.tstamelos.intelligentLogisticsAPI.record.ResponseId;
import com.tstamelos.intelligentLogisticsAPI.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ResponseId> createOrder(@RequestBody OrderParams params) {
        Long id = orderService.createOrder(params);
        return ResponseEntity
                .accepted()
                .body(new ResponseId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseId> updateOrder(@PathVariable Long id, @RequestBody OrderParams params) {
        Long idResponse = orderService.updateOrder(id, params);
        return ResponseEntity.ok(new ResponseId(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}


