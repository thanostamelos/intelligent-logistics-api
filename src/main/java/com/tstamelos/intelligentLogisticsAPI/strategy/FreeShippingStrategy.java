package com.tstamelos.intelligentLogisticsAPI.strategy;

import com.tstamelos.intelligentLogisticsAPI.model.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("FREE")
public class FreeShippingStrategy implements PricingStrategy {

    @Override
    public BigDecimal getCost(Order order) {
        return BigDecimal.ZERO;
    }
}
