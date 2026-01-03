package com.tstamelos.intelligentLogisticsAPI.model;

import com.tstamelos.intelligentLogisticsAPI.enums.OrderStatus;
import com.tstamelos.intelligentLogisticsAPI.enums.ShippingType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    private BigDecimal weight;

    private String destination;

    @Enumerated(EnumType.STRING)
    private ShippingType shippingType;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal cost;

    private String createdBy;

    private String updatedBy;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    @Version
    private Long version;
}
