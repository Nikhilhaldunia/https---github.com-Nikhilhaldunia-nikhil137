package com.shopsphere.service;

import com.shopsphere.model.Product;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryService {
    private final Map<Integer, Product> products = new TreeMap<>();
    private int nextId = 1;

    public Product addProduct(String name, String category, BigDecimal price, int stock) {
        Product p = new Product(nextId++, name, category, price, stock);
        products.put(p.getId(), p);
        return p;
    }

    public Optional<Product> findById(int id) {
        return Optional.ofNullable(products.get(id));
    }

    public List<Product> listAll() {
        return new ArrayList<>(products.values());
    }

    public List<Product> search(String keyword) {
        String k = keyword.toLowerCase();
        return products.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(k) || p.getCategory().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }

    public void seed() {
        addProduct("Wireless Mouse", "Electronics", new BigDecimal("24.99"), 50);
        addProduct("Mechanical Keyboard", "Electronics", new BigDecimal("89.90"), 25);
        addProduct("27\" Monitor", "Electronics", new BigDecimal("279.00"), 10);
        addProduct("Running Shoes", "Fashion", new BigDecimal("64.50"), 40);
        addProduct("Denim Jacket", "Fashion", new BigDecimal("79.00"), 15);
        addProduct("Coffee Maker", "Home", new BigDecimal("59.95"), 20);
        addProduct("Desk Lamp", "Home", new BigDecimal("19.99"), 60);
        addProduct("Java in Depth (Book)", "Books", new BigDecimal("42.00"), 30);
    }
}
