package com.tstamelos.intelligentLogisticsAPI.strategy;

import com.tstamelos.intelligentLogisticsAPI.model.Order;

import java.math.BigDecimal;

public class PricingStrategyResolver implements PricingStrategy {
    @Override
    public BigDecimal getCost(Order order) {
        return null;
    }
}
