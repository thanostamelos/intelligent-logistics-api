package com.tstamelos.intelligentLogisticsAPI.strategy;

import com.tstamelos.intelligentLogisticsAPI.model.Order;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal getCost(Order order);
}
