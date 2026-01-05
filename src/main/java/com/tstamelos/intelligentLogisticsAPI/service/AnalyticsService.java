package com.tstamelos.intelligentLogisticsAPI.service;

import com.tstamelos.intelligentLogisticsAPI.enums.OrderStatus;
import com.tstamelos.intelligentLogisticsAPI.model.Order;
import com.tstamelos.intelligentLogisticsAPI.record.AnalyticsResponse;
import com.tstamelos.intelligentLogisticsAPI.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    private final OrderRepository orderRepository;

    public AnalyticsService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    private List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public AnalyticsResponse getAnalytics() {
        List<Order> orders = findAllOrders();

        BigDecimal totalRevenue = orders.stream()
                .map(Order::getCost)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var ordersByDestination = orders.stream()
                .filter(o -> o.getDestination() != null && !o.getDestination().trim().isEmpty())
                .collect(Collectors.groupingBy(Order::getDestination));

        var mostExpensive = orders.stream()
                .filter(o -> o.getCost() != null)
                .max(Comparator.comparing(Order::getCost))
                .orElse(null);

        var delayedOrderIds = orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELAYED)
                .map(Order::getId)
                .toList();

        return new AnalyticsResponse(
                totalRevenue,
                ordersByDestination,
                mostExpensive,
                delayedOrderIds
        );
    }
}
