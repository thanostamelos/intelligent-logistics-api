package com.tstamelos.intelligentLogisticsAPI.service;

import com.tstamelos.intelligentLogisticsAPI.model.Order;
import com.tstamelos.intelligentLogisticsAPI.strategy.PricingStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class PricingService {

    private final Map<String, PricingStrategy> strategies;

    public PricingService(Map<String, PricingStrategy> strategies) {
        this.strategies = strategies;
    }

    public BigDecimal calculateCost(Order order) {
        if (order.getShippingType() == null) {
            throw new IllegalArgumentException("Shipping type cannot be null");
        }

        PricingStrategy strategy = strategies.get(order.getShippingType().name());

        if (strategy == null) {
            throw new IllegalArgumentException(
                    "No pricing strategy for type: " + order.getShippingType()
            );
        }

        return strategy.getCost(order);
    }
}

