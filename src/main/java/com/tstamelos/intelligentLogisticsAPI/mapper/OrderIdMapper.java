package com.tstamelos.intelligentLogisticsAPI.mapper;

import com.tstamelos.intelligentLogisticsAPI.model.Order;
import com.tstamelos.intelligentLogisticsAPI.record.ResponseId;
import lombok.extern.apachecommons.CommonsLog;

import java.util.List;
import java.util.stream.Collectors;

@CommonsLog
public class OrderIdMapper {

    public static List<ResponseId> toResponseIds(List<Order> orders) {
        return orders.stream()
                .map(order -> new ResponseId(order.getId()))
                .collect(Collectors.toList());
    }
}
