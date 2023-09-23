package dev.rambris.springmultipledb.model;

import java.time.Instant;
import java.util.List;

public record Order(
        Integer orderId,
        int customerId,
        Instant purchasedAt,
        List<OrderLine> orderLines
) {
    public Order withOrderLines(List<OrderLine> orderLines) {
        return new Order(orderId, customerId, purchasedAt, orderLines);
    }
}
