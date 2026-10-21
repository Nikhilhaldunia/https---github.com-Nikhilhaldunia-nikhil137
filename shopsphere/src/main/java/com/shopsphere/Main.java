package com.shopsphere;

import com.shopsphere.model.*;
import com.shopsphere.service.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        inventory.seed();
        OrderService orders = new OrderService(inventory);

        System.out.println("=== Welcome to ShopSphere ===");
        System.out.print("Your name: ");
        String name = in.nextLine().trim();
        System.out.print("Your email: ");
        String email = in.nextLine().trim();
        Customer customer = new Customer(1, name.isEmpty() ? "Guest" : name, email);
        Cart cart = new Cart();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = in.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> inventory.listAll().forEach(System.out::println);
                    case "2" -> search(inventory);
                    case "3" -> addToCart(inventory, cart);
                    case "4" -> System.out.println(cart);
                    case "5" -> removeFromCart(inventory, cart);
                    case "6" -> {
                        Order o = orders.checkout(customer, cart);
                        System.out.println("Order placed!\n" + o);
                    }
                    case "7" -> {
                        List<Order> mine = orders.ordersFor(customer);
                        if (mine.isEmpty()) System.out.println("No orders yet.");
                        mine.forEach(o -> System.out.println(o + "\n"));
                    }
                    case "8" -> {
                        int id = readInt("Order id to cancel: ");
                        System.out.println(orders.cancel(id) ? "Order cancelled, stock restored." : "Cannot cancel that order.");
                    }
                    case "9" -> {
                        int id = readInt("Order id to advance (ship/deliver): ");
                        System.out.println(orders.advance(id) ? "Status updated." : "Cannot update that order.");
                    }
                    case "0" -> running = false;
                    default -> System.out.println("Unknown option.");
                }
            } catch (RuntimeException e) {
                System.out.println("! " + e.getMessage());
            }
        }
        System.out.println("Thanks for shopping with ShopSphere, " + customer.name() + "!");
    }

    private static void printMenu() {
        System.out.println("""

                --- Menu ---
                1) Browse products     6) Checkout
                2) Search products     7) My orders
                3) Add to cart         8) Cancel order
                4) View cart           9) Advance order status
                5) Remove from cart    0) Exit""");
        System.out.print("> ");
    }

    private static void search(InventoryService inv) {
        System.out.print("Keyword: ");
        List<Product> found = inv.search(in.nextLine().trim());
        if (found.isEmpty()) System.out.println("No matches.");
        else found.forEach(System.out::println);
    }

    private static void addToCart(InventoryService inv, Cart cart) {
        Product p = inv.findById(readInt("Product id: ")).orElseThrow(() -> new IllegalArgumentException("No such product."));
        cart.add(p, readInt("Quantity: "));
        System.out.println("Added " + p.getName() + " to cart.");
    }

    private static void removeFromCart(InventoryService inv, Cart cart) {
        Product p = inv.findById(readInt("Product id: ")).orElseThrow(() -> new IllegalArgumentException("No such product."));
        System.out.println(cart.remove(p) ? "Removed." : "That item isn't in your cart.");
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(in.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid number.");
        }
    }
}
