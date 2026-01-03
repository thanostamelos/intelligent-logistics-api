package com.tstamelos.intelligentLogisticsAPI.record;

import com.tstamelos.intelligentLogisticsAPI.model.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record AnalyticsResponse(BigDecimal totalRevenue,
                                Map<String, List<Order>> ordersByDestination,
                                Order mostExpensiveOrder,
                                List<Long> delayedOrderIds) {
}
