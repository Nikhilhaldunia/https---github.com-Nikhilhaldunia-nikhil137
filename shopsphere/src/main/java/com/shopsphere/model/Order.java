package com.shopsphere.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Order {
    private final int id;
    private final Customer customer;
    private final List<OrderLine> lines;
    private final BigDecimal subtotal;
    private final BigDecimal discount;
    private final LocalDateTime createdAt = LocalDateTime.now();
    private OrderStatus status = OrderStatus.PLACED;

    public Order(int id, Customer customer, List<OrderLine> lines, BigDecimal subtotal, BigDecimal discount) {
        this.id = id;
        this.customer = customer;
        this.lines = List.copyOf(lines);
        this.subtotal = subtotal;
        this.discount = discount;
    }

    public int getId() { return id; }
    public Customer getCustomer() { return customer; }
    public List<OrderLine> getLines() { return lines; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getDiscount() { return discount; }
    public BigDecimal getTotal() { return subtotal.subtract(discount); }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Order #%d | %s | %s | %s%n", id, customer.name(),
                createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), status));
        for (OrderLine l : lines) {
            sb.append(String.format("   %-24s x%-3d $%9.2f%n", l.productName(), l.quantity(), l.subtotal()));
        }
        sb.append(String.format("   Subtotal: $%.2f  Discount: -$%.2f  TOTAL: $%.2f", subtotal, discount, getTotal()));
        return sb.toString();
    }
}
