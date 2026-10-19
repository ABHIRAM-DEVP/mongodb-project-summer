package com.SmartStockPro.app.controller;

import com.SmartStockPro.app.model.Sale;
import com.SmartStockPro.app.service.SaleService;
import com.SmartStockPro.app.service.AnalyticsService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    @Autowired
    private SaleService saleService;
    @Autowired
    private AnalyticsService analyticsService;

    @PostMapping
    public ResponseEntity<Sale> recordSale(@RequestBody Sale sale) {
        try {
            return ResponseEntity.ok(saleService.recordSale(sale));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        return ResponseEntity.ok(saleService.getAllSales());
    }
    @GetMapping("/report")
    public ResponseEntity<List<Document>> getSalesReport() {
        return ResponseEntity.ok(analyticsService.getMonthlySalesReport());
    }
}
