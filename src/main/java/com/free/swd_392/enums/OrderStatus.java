package com.free.swd_392.enums;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public enum OrderStatus {
    NEW,
    PAID,
    FAILED,
    DELIVERED,
    RETURNED,
    COMPLETED;

    private static final Map<OrderStatus, Set<OrderStatus>> ORDER_STATUS_STATE_MAP;

    static {
        ORDER_STATUS_STATE_MAP = new ConcurrentHashMap<>();
        ORDER_STATUS_STATE_MAP.put(NEW, Set.of(PAID));
        ORDER_STATUS_STATE_MAP.put(PAID, Set.of(DELIVERED, FAILED));
        ORDER_STATUS_STATE_MAP.put(FAILED, Collections.emptySet());
        ORDER_STATUS_STATE_MAP.put(DELIVERED, Set.of(COMPLETED, RETURNED));
        ORDER_STATUS_STATE_MAP.put(COMPLETED, Set.of(RETURNED));
        ORDER_STATUS_STATE_MAP.put(RETURNED, Collections.emptySet());
    }

    public static Set<OrderStatus> getAvailableStates(OrderStatus current) {
        return ORDER_STATUS_STATE_MAP.get(current);
    }

    public static boolean validateNextState(OrderStatus current, OrderStatus next) {
        var availableStateSet = getAvailableStates(current);
        if (availableStateSet == null) {
            return true;
        }
        return availableStateSet.contains(next);
    }
}
