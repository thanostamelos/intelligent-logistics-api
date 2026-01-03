package com.tstamelos.intelligentLogisticsAPI.model;

import com.tstamelos.intelligentLogisticsAPI.enums.VehicleStatus;
import com.tstamelos.intelligentLogisticsAPI.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    @Column(unique = true, nullable = false)
    private String licensePlate;

    private BigDecimal capacity; //kg

    private BigDecimal speed; //km/h

    private String color;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    private BigDecimal batteryLevel;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    @Version
    private Long version;
}
