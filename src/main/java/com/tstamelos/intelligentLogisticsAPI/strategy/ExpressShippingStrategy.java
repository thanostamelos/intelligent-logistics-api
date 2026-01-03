package com.tstamelos.intelligentLogisticsAPI.strategy;

import com.tstamelos.intelligentLogisticsAPI.model.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("EXPRESS")
public class ExpressShippingStrategy implements PricingStrategy {

    @Override
    public BigDecimal getCost(Order order) {
        return order.getWeight()
                .multiply(BigDecimal.valueOf(2.5)) // (Βάρος * 2.5€)
                .multiply(BigDecimal.valueOf(1.5)); // + 50% προσαύξηση
    }
}
