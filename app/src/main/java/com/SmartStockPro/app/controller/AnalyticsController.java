package com.SmartStockPro.app.controller;

import com.SmartStockPro.app.service.AnalyticsService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/category-inventory")
    public ResponseEntity<List<Document>> getCategoryWiseInventoryReport() {
        return ResponseEntity.ok(analyticsService.getCategoryWiseInventoryReport());
    }

    @GetMapping("/monthly-sales")
    public ResponseEntity<List<Document>> getMonthlySalesReport() {
        return ResponseEntity.ok(analyticsService.getMonthlySalesReport());
    }

    @GetMapping("/top-selling")
    public ResponseEntity<List<Document>> getTopSellingProductsReport() {
        return ResponseEntity.ok(analyticsService.getTopSellingProductsReport());
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Document>> getLowStockReport() {
        return ResponseEntity.ok(analyticsService.getLowStockReport());
    }

    @GetMapping("/expiring")
    public ResponseEntity<List<Document>> getExpiringProductsReport() {
        return ResponseEntity.ok(analyticsService.getExpiringProductsReport());
    }
}
