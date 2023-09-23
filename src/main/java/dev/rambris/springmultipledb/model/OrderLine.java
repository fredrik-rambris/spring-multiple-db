package dev.rambris.springmultipledb.model;

public record OrderLine(
        Integer orderLineId,
        Integer orderId,
        int productId,
        int quantity
) {
}
