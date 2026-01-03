package com.tstamelos.intelligentLogisticsAPI.service;

import com.tstamelos.intelligentLogisticsAPI.enums.OrderStatus;
import com.tstamelos.intelligentLogisticsAPI.model.Order;
import com.tstamelos.intelligentLogisticsAPI.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class OrderProcessor {
    private final OrderRepository orderRepository;
    private final PricingService pricingService;

    public OrderProcessor(OrderRepository orderRepository, PricingService pricingService) {
        this.orderRepository = orderRepository;
        this.pricingService = pricingService;
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processOrder(Long orderId) {
        log.info("Async processing started for order: {}", orderId);

        Optional<Order> orderOpt = findOrderWithRetry(orderId, 3);

        orderOpt.ifPresentOrElse(order -> {
            try {
                // Βήμα 1: Θέτουμε την παραγγελία σε κατάσταση επεξεργασίας
                order.setStatus(OrderStatus.PROCESSING);
                order.setUpdatedOn(LocalDateTime.now());
                order.setUpdatedBy("SYSTEM");
                orderRepository.saveAndFlush(order);

                // Προσομοίωση καθυστέρησης (π.χ. υπολογισμός δρομολογίου)
                Thread.sleep(3000);

                // Βήμα 2: Υπολογισμός κόστους μέσω του PricingService
                BigDecimal cost = pricingService.calculateCost(order);
                order.setCost(cost);

                // Βήμα 3: Ολοκλήρωση επεξεργασίας
                order.setStatus(OrderStatus.COMPLETED);
                order.setUpdatedOn(LocalDateTime.now());
                order.setUpdatedBy("SYSTEM");

                orderRepository.save(order);
                log.info("Order {} processed successfully with cost: {}", orderId, cost);

            } catch (Exception e) {
                log.error("Error processing order {}: {}", orderId, e.getMessage());
                order.setStatus(OrderStatus.FAILED);
                orderRepository.save(order);
            }
        }, () -> {
            log.error("CRITICAL: Async process failed. Order ID {} not found in database after retries.", orderId);
        });
    }

    /**
     * Βοηθητική μέθοδος που προσπαθεί να βρει την παραγγελία, περιμένοντας λίγο ανάμεσα στις προσπάθειες.
     * Αυτό δίνει χρόνο στο Transaction του Service να κάνει commit.
     */
    private Optional<Order> findOrderWithRetry(Long orderId, int maxRetries) {
        for (int i = 0; i < maxRetries; i++) {
            Optional<Order> order = orderRepository.findById(orderId);
            if (order.isPresent()) {
                return order;
            }
            try {
                log.info("Order {} not found yet, retrying in 500ms... (Attempt {}/{})", orderId, i + 1, maxRetries);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return Optional.empty();
    }
}
