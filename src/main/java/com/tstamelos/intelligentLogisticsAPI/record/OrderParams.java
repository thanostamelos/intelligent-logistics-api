package com.tstamelos.intelligentLogisticsAPI.record;

import java.math.BigDecimal;

public record OrderParams(String customerName, BigDecimal weight, String destination, String shippingType,
                          Long version) {
}
