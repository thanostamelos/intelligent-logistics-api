package com.tstamelos.intelligentLogisticsAPI.service;

import com.tstamelos.intelligentLogisticsAPI.enums.VehicleStatus;
import com.tstamelos.intelligentLogisticsAPI.enums.VehicleType;
import com.tstamelos.intelligentLogisticsAPI.exception.OrderValidationException;
import com.tstamelos.intelligentLogisticsAPI.exception.VehicleNotFoundException;
import com.tstamelos.intelligentLogisticsAPI.exception.VehicleValidationException;
import com.tstamelos.intelligentLogisticsAPI.exception.VersionMismatchException;
import com.tstamelos.intelligentLogisticsAPI.factory.VehicleFactory;
import com.tstamelos.intelligentLogisticsAPI.model.Vehicle;
import com.tstamelos.intelligentLogisticsAPI.record.VehicleParams;
import com.tstamelos.intelligentLogisticsAPI.repository.VehicleRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleFactory vehicleFactory;

    public VehicleService(VehicleRepository vehicleRepository, VehicleFactory vehicleFactory) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleFactory = vehicleFactory;
    }

    @Transactional
    public Long createVehicle(VehicleParams params) {
        if (vehicleRepository.existsByLicensePlate(params.licensePlate())) {
            throw new VehicleValidationException("Vehicle with license plate " + params.licensePlate() + " already exists.");
        }

        VehicleType type = parseVehicleType(params.vehicleType());

        Vehicle newVehicle = vehicleFactory.createVehicle(type, params.licensePlate());

        if (params.color() != null) {
            newVehicle.setColor(params.color());
        }

        validateVehicle(newVehicle);

        return vehicleRepository.save(newVehicle).getId();
    }

    @Transactional
    public Long updateVehicle(Long id, VehicleParams params) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));

        if (params.version() == null) {
            throw new OrderValidationException("Version is required");
        }

        if (!params.version().equals(existingVehicle.getVersion())) {
            throw new VersionMismatchException(
                    String.format("Vehicle with ID %d has been modified by another user. (Expected version: %d, but received: %d)",
                            id, existingVehicle.getVersion(), params.version())
            );
        }

        if (params.licensePlate() != null) existingVehicle.setLicensePlate(params.licensePlate());
        if (params.color() != null) existingVehicle.setColor(params.color());
        if (params.status() != null) existingVehicle.setStatus(parseVehicleStatus(params.status()));
        if (params.batteryLevel() != null) existingVehicle.setBatteryLevel(params.batteryLevel());

        existingVehicle.setUpdatedOn(LocalDateTime.now());

        validateVehicle(existingVehicle);

        return vehicleRepository.save(existingVehicle).getId();
    }

    private void validateVehicle(Vehicle vehicle) {
        if (vehicle.getLicensePlate() == null || vehicle.getLicensePlate().isBlank()) {
            throw new VehicleValidationException("License plate is required");
        }

        if (vehicle.getCapacity() == null || vehicle.getCapacity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new VehicleValidationException("Capacity must be greater than 0");
        }

        if (vehicle.getSpeed() == null || vehicle.getSpeed().compareTo(BigDecimal.ZERO) <= 0) {
            throw new VehicleValidationException("Speed must be greater than 0");
        }
    }

    private VehicleType parseVehicleType(String value) {
        if (value == null || value.isBlank()) {
            throw new VehicleValidationException("Vehicle type is required");
        }
        try {
            return VehicleType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new VehicleValidationException("Invalid vehicle type: " + value);
        }
    }

    private VehicleStatus parseVehicleStatus(String value) {
        try {
            return VehicleStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new VehicleValidationException("Invalid vehicle status: " + value);
        }
    }
}
