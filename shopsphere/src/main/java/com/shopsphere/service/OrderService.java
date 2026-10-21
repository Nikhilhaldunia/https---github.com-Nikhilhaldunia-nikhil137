package com.shopsphere.service;

import com.shopsphere.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class OrderService {
    private static final BigDecimal DISCOUNT_THRESHOLD = new BigDecimal("300");
    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.10");

    private final InventoryService inventory;
    private final Map<Integer, Order> orders = new LinkedHashMap<>();
    private int nextId = 1000;

    public OrderService(InventoryService inventory) {
        this.inventory = inventory;
    }

    public Order checkout(Customer customer, Cart cart) {
        if (cart.isEmpty()) throw new IllegalStateException("Cart is empty.");

        // Validate stock first so the order is all-or-nothing
        cart.getItems().forEach((p, q) -> {
            if (q > p.getStock()) throw new IllegalStateException("Insufficient stock for " + p.getName());
        });

        List<OrderLine> lines = new ArrayList<>();
        cart.getItems().forEach((p, q) -> {
            p.setStock(p.getStock() - q);
            lines.add(new OrderLine(p.getId(), p.getName(), p.getPrice(), q));
        });

        BigDecimal subtotal = cart.total();
        BigDecimal discount = subtotal.compareTo(DISCOUNT_THRESHOLD) >= 0
                ? subtotal.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        Order order = new Order(nextId++, customer, lines, subtotal, discount);
        orders.put(order.getId(), order);
        cart.clear();
        return order;
    }

    public boolean cancel(int orderId) {
        Order o = orders.get(orderId);
        if (o == null || o.getStatus() != OrderStatus.PLACED) return false;
        o.setStatus(OrderStatus.CANCELLED);
        for (OrderLine l : o.getLines()) {
            inventory.findById(l.productId()).ifPresent(p -> p.setStock(p.getStock() + l.quantity()));
        }
        return true;
    }

    public boolean advance(int orderId) {
        Order o = orders.get(orderId);
        if (o == null) return false;
        switch (o.getStatus()) {
            case PLACED -> o.setStatus(OrderStatus.SHIPPED);
            case SHIPPED -> o.setStatus(OrderStatus.DELIVERED);
            default -> { return false; }
        }
        return true;
    }

    public List<Order> ordersFor(Customer c) {
        return orders.values().stream().filter(o -> o.getCustomer().equals(c)).toList();
    }
}
