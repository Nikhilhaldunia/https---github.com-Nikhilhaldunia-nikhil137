# ShopSphere

A console-based e-commerce application in Java 17.

## Features
- Product catalogue with categories, stock and search
- Shopping cart with stock validation
- Checkout (all-or-nothing) with 10% discount on orders of $300+
- Order history, cancellation (restores stock) and status progression
  (PLACED -> SHIPPED -> DELIVERED)

## Structure
    com.shopsphere
      Main                    console UI
      model/                  Product, Customer, Cart, Order, OrderLine, OrderStatus
      service/                InventoryService, OrderService

## Run
    mvn package
    java -jar target/shopsphere-1.0.0.jar

Without Maven:

    mkdir out && javac -d out $(find src -name "*.java")
    java -cp out com.shopsphere.Main

## Ideas to extend
JDBC/JPA persistence, Spring Boot REST API, user accounts, payments, JUnit tests.
