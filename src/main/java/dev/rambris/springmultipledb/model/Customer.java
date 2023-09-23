package dev.rambris.springmultipledb.model;

public record Customer(
        Integer customerId,
        String name,
        String email
) {
}
