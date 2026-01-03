package com.tstamelos.intelligentLogisticsAPI.record;

import java.math.BigDecimal;

public record VehicleParams(String vehicleType, String licensePlate,
                            String color, String status, BigDecimal batteryLevel, Long version) {
}