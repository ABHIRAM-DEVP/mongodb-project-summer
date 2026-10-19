package com.SmartStockPro.app.model;

import lombok.AllArgsConstructor;
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
}
