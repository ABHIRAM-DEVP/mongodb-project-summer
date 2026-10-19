package com.SmartStockPro.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Document(collection = "inventory_details")
public class InventoryDetails {
    @Id
    private String id;
    private String productId;
    private String warehouseLocation;
    private String batchNumber;
    private LocalDate lastStockUpdate;
}
