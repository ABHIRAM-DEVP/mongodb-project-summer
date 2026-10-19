package com.SmartStockPro.app.repository;

import com.SmartStockPro.app.model.InventoryDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryDetailsRepository extends MongoRepository<InventoryDetails, String> {
    void deleteByProductId(String productId);
    InventoryDetails findByProductId(String productId);
}
