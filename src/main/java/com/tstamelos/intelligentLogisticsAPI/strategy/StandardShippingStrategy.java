package com.tstamelos.intelligentLogisticsAPI.strategy;

import com.tstamelos.intelligentLogisticsAPI.model.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("STANDARD")
public class StandardShippingStrategy implements PricingStrategy {

    @Override
    public BigDecimal getCost(Order order) {
        return order.getWeight().multiply(BigDecimal.valueOf(2.5));
    }
}
