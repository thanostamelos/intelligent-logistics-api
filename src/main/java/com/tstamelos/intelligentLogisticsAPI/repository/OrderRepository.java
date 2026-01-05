package com.tstamelos.intelligentLogisticsAPI.repository;

import com.tstamelos.intelligentLogisticsAPI.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
