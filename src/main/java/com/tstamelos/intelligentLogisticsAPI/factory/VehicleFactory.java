package com.tstamelos.intelligentLogisticsAPI.factory;

import com.tstamelos.intelligentLogisticsAPI.enums.VehicleStatus;
import com.tstamelos.intelligentLogisticsAPI.enums.VehicleType;
import com.tstamelos.intelligentLogisticsAPI.exception.VehicleValidationException;
import com.tstamelos.intelligentLogisticsAPI.model.Vehicle;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class VehicleFactory {
    public Vehicle createVehicle(VehicleType type, String licensePlate) {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(type);
        vehicle.setLicensePlate(licensePlate);
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        vehicle.setBatteryLevel(BigDecimal.valueOf(100));
        vehicle.setCreatedOn(LocalDateTime.now());

        switch (type) {
            case TRUCK:
                vehicle.setCapacity(new BigDecimal("5000.0"));
                vehicle.setSpeed(new BigDecimal("90.0"));
                break;
            case VAN:
                vehicle.setCapacity(new BigDecimal("1500.0"));
                vehicle.setSpeed(new BigDecimal("110.0"));
                break;
            case DRONE:
                vehicle.setCapacity(new BigDecimal("10.0"));
                vehicle.setSpeed(new BigDecimal("60.0"));
                break;
            default:
                throw new VehicleValidationException("Vehicle type " + type + " is not supported by the factory yet.");
        }
        return vehicle;
    }
}
