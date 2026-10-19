package com.SmartStockPro.app.repository;

import com.SmartStockPro.app.model.Reminder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderRepository extends MongoRepository<Reminder, String> {
}
