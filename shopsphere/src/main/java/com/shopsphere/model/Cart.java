package com.shopsphere.model;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
    private final Map<Product, Integer> items = new LinkedHashMap<>();

    public void add(Product product, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be positive.");
        int newQty = items.getOrDefault(product, 0) + qty;
        if (newQty > product.getStock()) {
            throw new IllegalArgumentException("Only " + product.getStock() + " in stock for " + product.getName());
        }
        items.put(product, newQty);
    }

    public boolean remove(Product product) {
        return items.remove(product) != null;
    }

    public void clear() { items.clear(); }

    public boolean isEmpty() { return items.isEmpty(); }

    public Map<Product, Integer> getItems() { return Map.copyOf(items); }

    public BigDecimal total() {
        return items.entrySet().stream()
                .map(e -> e.getKey().getPrice().multiply(BigDecimal.valueOf(e.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        if (items.isEmpty()) return "Your cart is empty.";
        StringBuilder sb = new StringBuilder();
        items.forEach((p, q) -> sb.append(String.format("  %-24s x%-3d $%9.2f%n",
                p.getName(), q, p.getPrice().multiply(BigDecimal.valueOf(q)))));
        sb.append(String.format("  Cart total: $%.2f", total()));
        return sb.toString();
    }
}
