package com.SmartStockPro.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Document(collection = "sales")
public class Sale {
    @Id
    private String id;
    private String productId;
    private int quantitySold;
    private LocalDateTime saleDate;

    public Sale(String id, String productId, int quantitySold, LocalDateTime saleDate) {
        this.id = id;
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.saleDate = saleDate;
    }
}

