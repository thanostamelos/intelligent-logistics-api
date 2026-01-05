package com.tstamelos.intelligentLogisticsAPI.service;

import com.tstamelos.intelligentLogisticsAPI.enums.OrderStatus;
import com.tstamelos.intelligentLogisticsAPI.enums.ShippingType;
import com.tstamelos.intelligentLogisticsAPI.exception.OrderNotFoundException;
import com.tstamelos.intelligentLogisticsAPI.exception.OrderValidationException;
import com.tstamelos.intelligentLogisticsAPI.exception.VersionMismatchException;
import com.tstamelos.intelligentLogisticsAPI.model.Order;
import com.tstamelos.intelligentLogisticsAPI.record.OrderParams;
import com.tstamelos.intelligentLogisticsAPI.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProcessor orderProcessor;

    public OrderService(OrderRepository orderRepository, OrderProcessor orderProcessor) {
        this.orderRepository = orderRepository;
        this.orderProcessor = orderProcessor;
    }

    @Transactional
    public Long createOrder(OrderParams params) {
        ShippingType shippingType = parseShippingType(params.shippingType());

        Order newOrder = Order.builder()
                .customerName(params.customerName())
                .weight(params.weight())
                .destination(params.destination())
                .shippingType(shippingType)
                .status(OrderStatus.PENDING)
                .createdOn(LocalDateTime.now())
                .createdBy(params.customerName())
                .build();

        validateOrder(newOrder);
        Order savedOrder = orderRepository.saveAndFlush(newOrder);

        // Το Propagation.REQUIRES_NEW στον Processor θα τον κρατήσει απομονωμένο.
        orderProcessor.processOrder(savedOrder.getId());

        return savedOrder.getId();
    }

    @Transactional
    public Long updateOrder(Long id, OrderParams params) {
        if (id == null) {
            throw new OrderValidationException("Order ID is required for update");
        }
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (params.version() == null) {
            throw new OrderValidationException("Version is required");
        }

        if (!params.version().equals(existing.getVersion())) {
            throw new VersionMismatchException(
                    String.format("Order with ID %d has been modified by another user. (Expected version: %d, but received: %d)",
                            id, existing.getVersion(), params.version())
            );
        }

        boolean weightChanged = params.weight() != null && !params.weight().equals(existing.getWeight());
        boolean shippingChanged = false;

        if (params.shippingType() != null) {
            ShippingType newType = parseShippingType(params.shippingType());
            shippingChanged = (newType != existing.getShippingType());
            existing.setShippingType(newType);
        }

        boolean needsReprocessing = weightChanged || shippingChanged;

        if (params.weight() != null) existing.setWeight(params.weight());
        if (params.customerName() != null) existing.setCustomerName(params.customerName());
        if (params.destination() != null) existing.setDestination(params.destination());

        if (needsReprocessing) {
            existing.setStatus(OrderStatus.PENDING);
            existing.setCost(null); // Μηδενίζουμε το παλιό (λάθος) κόστος
        }

        existing.setUpdatedOn(LocalDateTime.now());
        existing.setUpdatedBy(params.customerName() != null ? params.customerName() : existing.getCustomerName());

        validateOrder(existing);
        Long savedId = orderRepository.saveAndFlush(existing).getId();

        if (needsReprocessing) {
            orderProcessor.processOrder(savedId);
        }

        return savedId;
    }

    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == OrderStatus.PROCESSING || order.getStatus() == OrderStatus.COMPLETED) {
            throw new OrderValidationException("Order cannot be canceled because it is already " + order.getStatus());
        }

        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new OrderValidationException("Order is already canceled");
        }

        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedOn(LocalDateTime.now());
        order.setUpdatedBy(order.getCreatedBy());

        orderRepository.save(order);
    }

    private void validateOrder(Order order) {
        if (order.getCustomerName() == null || order.getCustomerName().isBlank()) {
            throw new OrderValidationException("Customer name is required");
        }

        if (order.getWeight() == null || order.getWeight().signum() <= 0) {
            throw new OrderValidationException("Weight must be greater than 0");
        }

        if (order.getDestination() == null || order.getDestination().isBlank()) {
            throw new OrderValidationException("Destination is required");
        }
    }

    private ShippingType parseShippingType(String value) {

        if (value == null || value.isBlank()) {
            throw new OrderValidationException("Shipping type is required");
        }

        try {
            return ShippingType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new OrderValidationException("Invalid shipping type: " + value);
        }
    }

}
