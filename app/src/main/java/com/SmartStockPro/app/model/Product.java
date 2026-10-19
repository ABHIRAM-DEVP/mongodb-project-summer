package com.SmartStockPro.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private double price;
    private int quantity;
    private String categoryId;
    private String supplierId;
    private LocalDate expiryDate;
    private int threshold;

    @DocumentReference
    private InventoryDetails inventoryDetails;

    public Product(String id, String name, double price, int quantity, String categoryId,
                   String supplierId, LocalDate expiryDate, int threshold, InventoryDetails inventoryDetails) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.expiryDate = expiryDate;
        this.threshold = threshold;
        this.inventoryDetails = inventoryDetails;
    }
}

