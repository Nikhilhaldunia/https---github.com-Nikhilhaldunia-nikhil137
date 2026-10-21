package com.shopsphere.model;

import java.math.BigDecimal;

public class Product {
    private final int id;
    private final String name;
    private final String category;
    private final BigDecimal price;
    private int stock;

    public Product(int id, String name, String category, BigDecimal price, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return String.format("#%-3d %-24s %-12s $%9.2f  stock: %d", id, name, category, price, stock);
    }
}
