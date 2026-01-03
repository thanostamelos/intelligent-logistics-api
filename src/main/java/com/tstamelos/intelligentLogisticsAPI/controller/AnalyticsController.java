package com.tstamelos.intelligentLogisticsAPI.controller;

import com.tstamelos.intelligentLogisticsAPI.record.AnalyticsResponse;
import com.tstamelos.intelligentLogisticsAPI.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public AnalyticsResponse getAnalytics() {
        return analyticsService.getAnalytics();
    }
}

