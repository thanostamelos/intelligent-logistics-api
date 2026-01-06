package com.tstamelos.intelligentLogisticsAPI.repository;

import com.tstamelos.intelligentLogisticsAPI.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByLicensePlate(String licensePlate);
}
