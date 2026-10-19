package com.SmartStockPro.app.service;

import com.SmartStockPro.app.model.Product;
import com.SmartStockPro.app.model.Reminder;
import com.SmartStockPro.app.repository.ProductRepository;
import com.SmartStockPro.app.repository.ReminderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SchedulerService {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Scheduled(cron = "0 0 8 * * ?")
    public void runDailyChecks() {
        logger.info("Executing scheduled inventory checks at 8:00 AM...");
        performInventoryChecks();
    }

    @Scheduled(fixedRate = 60000)
    public void runFrequentChecksForDemo() {
        logger.info("Executing scheduled inventory checks (Demo Frequency)...");
        performInventoryChecks();
    }

    private void performInventoryChecks() {
        List<Product> allProducts = productRepository.findAll();
        for (Product product : allProducts) {
            if (product.getQuantity() < product.getThreshold()) {
                String message = String.format("ALERT: Low stock for product '%s' (ID: %s). Current Qty: %d, Threshold: %d", 
                    product.getName(), product.getId(), product.getQuantity(), product.getThreshold());
                logger.warn(message);
                Reminder reminder = new Reminder(null, message, LocalDateTime.now());
                reminderRepository.save(reminder);
            }
            if (product.getExpiryDate() != null) {
                LocalDate targetDate = LocalDate.now().plusDays(30);
                if (product.getExpiryDate().isBefore(targetDate) || product.getExpiryDate().isEqual(targetDate)) {
                    String message = String.format("ALERT: Product '%s' (ID: %s) is nearing expiry or expired. Expiry Date: %s. Current Qty: %d",
                        product.getName(), product.getId(), product.getExpiryDate(), product.getQuantity());
                    logger.warn(message);

                    Reminder reminder = new Reminder(null, message, LocalDateTime.now());
                    reminderRepository.save(reminder);
                }
            }
        }
    }
}
