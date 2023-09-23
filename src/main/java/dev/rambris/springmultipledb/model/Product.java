package dev.rambris.springmultipledb.model;

public record Product(
        Integer productId,
        String name,
        String sku,
        int stock
) {
}
