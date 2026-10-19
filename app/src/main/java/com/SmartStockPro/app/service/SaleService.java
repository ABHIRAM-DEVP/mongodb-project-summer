package com.SmartStockPro.app.service;

import com.SmartStockPro.app.model.Sale;
import com.SmartStockPro.app.model.Product;
import com.SmartStockPro.app.repository.SaleRepository;
import com.SmartStockPro.app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleService {
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    public Sale recordSale(Sale sale) {
        Product product = productRepository.findById(sale.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found for sale: " + sale.getProductId()));

        if (product.getQuantity() < sale.getQuantitySold()) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName() + 
                    ". Available: " + product.getQuantity() + ", Requested: " + sale.getQuantitySold());
        }
        product.setQuantity(product.getQuantity() - sale.getQuantitySold());
        productRepository.save(product);
        if (sale.getSaleDate() == null) {
            sale.setSaleDate(LocalDateTime.now());
        }
        return saleRepository.save(sale);
    }
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }
}
