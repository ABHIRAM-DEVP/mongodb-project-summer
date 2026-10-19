package com.SmartStockPro.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "suppliers")
public class Supplier {
    @Id
    private String id;
    private String supplierName;
    private String contact;

    public Supplier(String id, String supplierName, String contact) {
        this.id = id;
        this.supplierName = supplierName;
        this.contact = contact;
    }
}

