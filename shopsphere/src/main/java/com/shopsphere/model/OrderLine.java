package com.shopsphere.model;

import java.math.BigDecimal;

public record OrderLine(int productId, String productName, BigDecimal unitPrice, int quantity) {
    public BigDecimal subtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
