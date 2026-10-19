package com.SmartStockPro.app.service;

import com.SmartStockPro.app.model.Product;
import com.SmartStockPro.app.model.InventoryDetails;
import com.SmartStockPro.app.repository.ProductRepository;
import com.SmartStockPro.app.repository.InventoryDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryDetailsRepository inventoryDetailsRepository;

    public Product createProduct(Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new RuntimeException("Product with name " + product.getName() + " already exists.");
        }
        
        InventoryDetails details = product.getInventoryDetails();
        product.setInventoryDetails(null);
        Product savedProduct = productRepository.save(product);

        if (details != null) {
            details.setProductId(savedProduct.getId());
            if (details.getLastStockUpdate() == null) {
                details.setLastStockUpdate(LocalDate.now());
            }
            details = inventoryDetailsRepository.save(details);
            savedProduct.setInventoryDetails(details);
            savedProduct = productRepository.save(savedProduct);
        }
        return savedProduct;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Product updateProduct(String id, Product productDetails) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        product.setCategoryId(productDetails.getCategoryId());
        product.setSupplierId(productDetails.getSupplierId());
        product.setExpiryDate(productDetails.getExpiryDate());
        product.setThreshold(productDetails.getThreshold());

        if (productDetails.getInventoryDetails() != null) {
            InventoryDetails currentDetails = product.getInventoryDetails();
            InventoryDetails newDetails = productDetails.getInventoryDetails();
            if (currentDetails != null) {
                newDetails.setId(currentDetails.getId());
            }
            newDetails.setProductId(id);
            newDetails.setLastStockUpdate(LocalDate.now());
            newDetails = inventoryDetailsRepository.save(newDetails);
            product.setInventoryDetails(newDetails);
        }

        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        
        if (product.getInventoryDetails() != null) {
            inventoryDetailsRepository.deleteById(product.getInventoryDetails().getId());
        } else {
            inventoryDetailsRepository.deleteByProductId(id);
        }
        productRepository.delete(product);
    }

    public List<Product> searchProductByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public String checkProductAvailability(String name) {
        List<Product> products = productRepository.findByName(name);
        if (products.isEmpty()) {
            return "Product not found: " + name;
        }
        int totalQuantity = products.stream().mapToInt(Product::getQuantity).sum();
        if (totalQuantity > 0) {
            return "Product: " + name + " is AVAILABLE. Current Stock: " + totalQuantity;
        } else {
            return "Product: " + name + " is OUT OF STOCK.";
        }
    }
}
