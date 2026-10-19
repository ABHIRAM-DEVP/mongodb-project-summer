package com.SmartStockPro.app.repository;

import com.SmartStockPro.app.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByName(String name);

    @Query("{ 'categoryId' : ?0 }")
    List<Product> findByCategory(String categoryId);
    List<Product> findByQuantityLessThan(int quantity);
    List<Product> findByExpiryDateBefore(LocalDate date);
    boolean existsByName(String name);
    List<Product> findByNameContainingIgnoreCase(String name);
}
