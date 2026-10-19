package com.SmartStockPro.app.config;

import com.SmartStockPro.app.model.*;
import com.SmartStockPro.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Order(1)
@Profile("!test")
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryDetailsRepository inventoryDetailsRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Override
    public void run(String... args) {

        if (categoryRepository.count() > 0) {
            return;
        }
        System.out.println("Seeding SmartStockPro Database...");


        Category dairy = categoryRepository.save(new Category(null, "Dairy Products"));
        Category grains = categoryRepository.save(new Category(null, "Grains & Flours"));
        Category beverages = categoryRepository.save(new Category(null, "Beverages"));
        Category snacks = categoryRepository.save(new Category(null, "Snacks & Biscuits"));
        Category personalCare = categoryRepository.save(new Category(null, "Personal Care"));

        Supplier amul = supplierRepository.save(new Supplier(null, "Amul Dairy", "+91-2692-256124"));
        Supplier itc = supplierRepository.save(new Supplier(null, "ITC Limited", "+91-33-22889371"));
        Supplier tata = supplierRepository.save(new Supplier(null, "Tata Consumer Products", "+91-22-66658282"));
        Supplier britannia = supplierRepository.save(new Supplier(null, "Britannia Industries", "+91-80-39400800"));
        Supplier hul = supplierRepository.save(new Supplier(null, "Hindustan Unilever", "+91-22-39830000"));


        Product p1 = productRepository.save(new Product(null, "Amul Gold Milk 1L", 66.0, 8,dairy.getId(), amul.getId(),LocalDate.now().plusDays(2), 15, null));

        Product p2 = productRepository.save(new Product(null, "Amul Butter 500g", 280.0, 20,dairy.getId(), amul.getId(),LocalDate.now().plusDays(20), 10, null));

        Product p3 = productRepository.save(new Product(null, "Aashirvaad Atta 10kg", 460.0, 40,grains.getId(), itc.getId(),LocalDate.now().plusMonths(3), 10, null));

        Product p4 = productRepository.save(new Product(null, "Tata Salt 1kg", 28.0, 150,grains.getId(), tata.getId(),LocalDate.now().plusMonths(24), 30, null));

        Product p5 = productRepository.save(new Product(null, "Tata Tea Gold", 220.0, 50,beverages.getId(), tata.getId(),LocalDate.now().plusMonths(12), 15, null));

        Product p6 = productRepository.save(new Product(null, "Bru Coffee 200g", 390.0, 30,beverages.getId(), hul.getId(),LocalDate.now().plusMonths(10), 10, null));

        Product p7 = productRepository.save(new Product(null, "Britannia Marie Gold", 35.0, 90,snacks.getId(), britannia.getId(),LocalDate.now().plusMonths(6), 20, null));

        Product p8 = productRepository.save(new Product(null, "Parle-G Biscuit", 20.0, 200,snacks.getId(), britannia.getId(),LocalDate.now().plusMonths(8), 50, null));
        Product p9 = productRepository.save(new Product(null, "Lux Soap Pack", 120.0, 7,personalCare.getId(), hul.getId(),LocalDate.now().plusMonths(18), 15, null));

        Product p10 = productRepository.save(new Product(null, "Dove Shampoo 340ml", 290.0, 22,personalCare.getId(), hul.getId(),LocalDate.now().plusMonths(15), 10, null));






        inventoryDetailsRepository.save(new InventoryDetails(null, p1.getId(),"Cold Storage A1", "MILK-001", LocalDate.now()));

        inventoryDetailsRepository.save(new InventoryDetails(null, p2.getId(),"Cold Storage A2", "BUTTER-002", LocalDate.now()));

        inventoryDetailsRepository.save(new InventoryDetails(null, p3.getId(),"Rack B1", "ATTA-003", LocalDate.now()));

        inventoryDetailsRepository.save(new InventoryDetails(null, p4.getId(),"Rack B2", "SALT-004", LocalDate.now()));

        inventoryDetailsRepository.save(new InventoryDetails(null, p5.getId(),"Rack C1", "TEA-005", LocalDate.now()));

        inventoryDetailsRepository.save(new InventoryDetails(null, p6.getId(),"Rack C2", "COFFEE-006", LocalDate.now()));

        inventoryDetailsRepository.save(new InventoryDetails(null, p7.getId(),"Rack D1", "BISCUIT-007", LocalDate.now()));

        inventoryDetailsRepository.save(new InventoryDetails(null, p8.getId(),"Rack D2", "PARLE-008", LocalDate.now()));

        inventoryDetailsRepository.save(new InventoryDetails(null, p9.getId(),"Rack E1", "SOAP-009", LocalDate.now()));

        inventoryDetailsRepository.save(new InventoryDetails(null, p10.getId(),"Rack E2", "SHAMPOO-010", LocalDate.now()));


        saleRepository.save(new Sale(null, p1.getId(), 20,LocalDateTime.now().minusMonths(5)));

        saleRepository.save(new Sale(null, p4.getId(), 50,LocalDateTime.now().minusMonths(5)));

        saleRepository.save(new Sale(null, p8.getId(), 100,LocalDateTime.now().minusMonths(4)));

        saleRepository.save(new Sale(null, p7.getId(), 40,LocalDateTime.now().minusMonths(4)));

        saleRepository.save(new Sale(null, p5.getId(), 30,LocalDateTime.now().minusMonths(3)));

        saleRepository.save(new Sale(null, p8.getId(), 120,LocalDateTime.now().minusMonths(2)));

        saleRepository.save(new Sale(null, p4.getId(), 80,LocalDateTime.now().minusMonths(2)));

        saleRepository.save(new Sale(null, p1.getId(), 60,LocalDateTime.now().minusMonths(1)));

        saleRepository.save(new Sale(null, p8.getId(), 150,LocalDateTime.now().minusDays(20)));

        saleRepository.save(new Sale(null, p10.getId(), 25,LocalDateTime.now().minusDays(10)));

        reminderRepository.save(new Reminder(null, "Low Stock Alert: Amul Gold Milk 1L",LocalDateTime.now()));

        reminderRepository.save(new Reminder(null,"Low Stock Alert: Lux Soap Pack",LocalDateTime.now()));

        reminderRepository.save(new Reminder(null,"Expiry Alert: Amul Gold Milk 1L",LocalDateTime.now()));

        reminderRepository.save(new Reminder(null,"Expiry Alert: Amul Butter 500g",LocalDateTime.now()));

        reminderRepository.save(new Reminder(null,"Daily Inventory Audit Completed",LocalDateTime.now()));

        System.out.println("SmartStockPro sample data inserted successfully.");
    }
}