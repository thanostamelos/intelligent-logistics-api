package com.tstamelos.intelligentLogisticsAPI.controller;

import com.tstamelos.intelligentLogisticsAPI.record.ResponseId;
import com.tstamelos.intelligentLogisticsAPI.record.VehicleParams;
import com.tstamelos.intelligentLogisticsAPI.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<ResponseId> createVehicle(@RequestBody VehicleParams vehicle) {
        Long id = vehicleService.createVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseId> updateVehicle(@PathVariable Long id, @RequestBody VehicleParams vehicle) {
        Long updatedId = vehicleService.updateVehicle(id, vehicle);
        return ResponseEntity.ok(new ResponseId(updatedId));
    }

    @PostMapping("/init")
    public ResponseEntity<String> initFleet() {
        vehicleService.createVehicle(new VehicleParams("TRUCK", "AED-124", "Blue", null, null, 0L));
        vehicleService.createVehicle(new VehicleParams("VAN", "XYE-783", "White", null, null, 0L));
        vehicleService.createVehicle(new VehicleParams("DRONE", "RRN-201", "Black", null, null, 0L));
        return ResponseEntity.ok("Fleet initialized using Factory Method Pattern");
    }
}
