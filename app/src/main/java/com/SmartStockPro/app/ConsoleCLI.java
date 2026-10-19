package com.SmartStockPro.app;

import com.SmartStockPro.app.model.*;
import com.SmartStockPro.app.service.*;
import com.SmartStockPro.app.util.TablePrinter;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Component
@Profile("!test")
public class ConsoleCLI implements CommandLineRunner {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private AnalyticsService analyticsService;

    @Override
    public void run(String... args) throws Exception {
        Thread cliThread = new Thread(this::runMenuLoop);
        cliThread.setDaemon(false);
        cliThread.start();
    }

    private void runMenuLoop() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }

        // Guard: only start interactive CLI when running in a real terminal
        if (System.console() == null) {
            System.out.println("\n[SmartStock Pro] Non-interactive terminal detected.");
            System.out.println("[SmartStock Pro] Run via: mvnw.cmd spring-boot:run");
            System.out.println("[SmartStock Pro] Swagger UI: http://localhost:8080/swagger-ui/index.html");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===========================================");
            System.out.println("*    SMARTSTOCK PRO INVENTORY SYSTEM CLI  *");
            System.out.println("===========================================");
            System.out.println("1. Product Management");
            System.out.println("2. Category Management");
            System.out.println("3. Supplier Management");
            System.out.println("4. Sales Management");
            System.out.println("5. View Analytics Reports");
            System.out.println("6. View System Reminders");
            System.out.println("7. Exit");
            System.out.println("===========================================");
            System.out.print("Select option (1-7): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    handleProductMenu(scanner);
                    break;
                case "2":
                    handleCategoryMenu(scanner);
                    break;
                case "3":
                    handleSupplierMenu(scanner);
                    break;
                case "4":
                    handleSalesMenu(scanner);
                    break;
                case "5":
                    handleAnalyticsMenu(scanner);
                    break;
                case "6":
                    handleRemindersMenu();
                    break;
                case "7":
                    System.out.println("Exiting Console CLI. The server is still running...");
                    return;
                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 7.");
            }
        }
    }

    private void handleProductMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Product Management ---");
            System.out.println("1. Add Product");
            System.out.println("2. List All Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Search Product By Name");
            System.out.println("6. Check Product Availability");
            System.out.println("7. Go Back");
            System.out.print("Select option: ");
            String op = scanner.nextLine().trim();
            switch (op) {
                case "1":
                    try {
                        System.out.print("Enter product name: ");
                        String name = scanner.nextLine().trim();
                        System.out.print("Enter price: ");
                        double price = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Enter quantity: ");
                        int quantity = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Category ID: ");
                        String catId = scanner.nextLine().trim();
                        System.out.print("Enter Supplier ID: ");
                        String supId = scanner.nextLine().trim();
                        System.out.print("Enter Expiry Date (YYYY-MM-DD) or press Enter to skip: ");
                        String expStr = scanner.nextLine().trim();
                        LocalDate expiryDate = expStr.isEmpty() ? null : LocalDate.parse(expStr);
                        System.out.print("Enter Low Stock Threshold: ");
                        int threshold = Integer.parseInt(scanner.nextLine().trim());

                        System.out.print("Do you want to add Inventory Details? (y/n): ");
                        String detailsOp = scanner.nextLine().trim();
                        InventoryDetails details = null;
                        if (detailsOp.equalsIgnoreCase("y")) {
                            System.out.print("Enter warehouse location (e.g., Warehouse A): ");
                            String loc = scanner.nextLine().trim();
                            System.out.print("Enter batch number: ");
                            String batch = scanner.nextLine().trim();
                            details = new InventoryDetails(null, null, loc, batch, LocalDate.now());
                        }

                        Product product = new Product(null, name, price, quantity, catId, supId, expiryDate, threshold,
                                details);
                        Product saved = productService.createProduct(product);
                        System.out.println("Product added successfully with ID: " + saved.getId());
                    } catch (Exception e) {
                        System.out.println("Error adding product: " + e.getMessage());
                    }
                    break;

                case "2":
                    TablePrinter.printProducts(productService.getAllProducts());
                    break;

                case "3":
                    try {
                        System.out.print("Enter product ID to update: ");
                        String id = scanner.nextLine().trim();
                        System.out.print("Enter new product name: ");
                        String name = scanner.nextLine().trim();
                        System.out.print("Enter new price: ");
                        double price = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Enter new quantity: ");
                        int quantity = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter new Category ID: ");
                        String catId = scanner.nextLine().trim();
                        System.out.print("Enter new Supplier ID: ");
                        String supId = scanner.nextLine().trim();
                        System.out.print("Enter new Expiry Date (YYYY-MM-DD) or press Enter to skip: ");
                        String expStr = scanner.nextLine().trim();
                        LocalDate expiryDate = expStr.isEmpty() ? null : LocalDate.parse(expStr);
                        System.out.print("Enter new Low Stock Threshold: ");
                        int threshold = Integer.parseInt(scanner.nextLine().trim());

                        System.out.print("Do you want to update/add Inventory Details? (y/n): ");
                        String detailsOp = scanner.nextLine().trim();
                        InventoryDetails details = null;
                        if (detailsOp.equalsIgnoreCase("y")) {
                            System.out.print("Enter warehouse location: ");
                            String loc = scanner.nextLine().trim();
                            System.out.print("Enter batch number: ");
                            String batch = scanner.nextLine().trim();
                            details = new InventoryDetails(null, id, loc, batch, LocalDate.now());
                        }

                        Product product = new Product(null, name, price, quantity, catId, supId, expiryDate, threshold,
                                details);
                        productService.updateProduct(id, product);
                        System.out.println("Product updated successfully.");
                    } catch (Exception e) {
                        System.out.println("Error updating product: " + e.getMessage());
                    }
                    break;

                case "4":
                    System.out.print("Enter product ID to delete: ");
                    String idToDelete = scanner.nextLine().trim();
                    try {
                        productService.deleteProduct(idToDelete);
                        System.out.println("Product deleted successfully.");
                    } catch (Exception e) {
                        System.out.println("Error deleting product: " + e.getMessage());
                    }
                    break;

                case "5":
                    System.out.print("Enter product name query: ");
                    String nameQuery = scanner.nextLine().trim();
                    TablePrinter.printProducts(productService.searchProductByName(nameQuery));
                    break;

                case "6":
                    System.out.print("Enter exact product name: ");
                    String availName = scanner.nextLine().trim();
                    System.out.println(productService.checkProductAvailability(availName));
                    break;

                case "7":
                    return;
            }
        }
    }

    private void handleCategoryMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Category Management ---");
            System.out.println("1. Add Category");
            System.out.println("2. List All Categories");
            System.out.println("3. Update Category");
            System.out.println("4. Delete Category");
            System.out.println("5. Go Back");
            System.out.print("Select option: ");
            String op = scanner.nextLine().trim();
            switch (op) {
                case "1":
                    System.out.print("Enter category name: ");
                    String name = scanner.nextLine().trim();
                    Category cat = new Category(null, name);
                    Category saved = categoryService.createCategory(cat);
                    System.out.println("Category created with ID: " + saved.getId());
                    break;

                case "2":
                    TablePrinter.printCategories(categoryService.getAllCategories());
                    break;

                case "3":
                    try {
                        System.out.print("Enter category ID to update: ");
                        String id = scanner.nextLine().trim();
                        System.out.print("Enter new category name: ");
                        String newName = scanner.nextLine().trim();
                        categoryService.updateCategory(id, new Category(null, newName));
                        System.out.println("Category updated successfully.");
                    } catch (Exception e) {
                        System.out.println("Error updating category: " + e.getMessage());
                    }
                    break;

                case "4":
                    System.out.print("Enter category ID to delete: ");
                    String idToDelete = scanner.nextLine().trim();
                    try {
                        categoryService.deleteCategory(idToDelete);
                        System.out.println("Category deleted successfully.");
                    } catch (Exception e) {
                        System.out.println("Error deleting category: " + e.getMessage());
                    }
                    break;

                case "5":
                    return;
            }
        }
    }

    private void handleSupplierMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Supplier Management ---");
            System.out.println("1. Add Supplier");
            System.out.println("2. List All Suppliers");
            System.out.println("3. Update Supplier");
            System.out.println("4. Delete Supplier");
            System.out.println("5. Go Back");
            System.out.print("Select option: ");
            String op = scanner.nextLine().trim();
            switch (op) {
                case "1":
                    System.out.print("Enter supplier name: ");
                    String name = scanner.nextLine().trim();
                    System.out.print("Enter supplier contact info: ");
                    String contact = scanner.nextLine().trim();
                    Supplier sup = new Supplier(null, name, contact);
                    Supplier saved = supplierService.createSupplier(sup);
                    System.out.println("Supplier created with ID: " + saved.getId());
                    break;

                case "2":
                    TablePrinter.printSuppliers(supplierService.getAllSuppliers());
                    break;

                case "3":
                    try {
                        System.out.print("Enter supplier ID to update: ");
                        String id = scanner.nextLine().trim();
                        System.out.print("Enter new supplier name: ");
                        String newName = scanner.nextLine().trim();
                        System.out.print("Enter new supplier contact info: ");
                        String newContact = scanner.nextLine().trim();
                        supplierService.updateSupplier(id, new Supplier(null, newName, newContact));
                        System.out.println("Supplier updated successfully.");
                    } catch (Exception e) {
                        System.out.println("Error updating supplier: " + e.getMessage());
                    }
                    break;

                case "4":
                    System.out.print("Enter supplier ID to delete: ");
                    String idToDelete = scanner.nextLine().trim();
                    try {
                        supplierService.deleteSupplier(idToDelete);
                        System.out.println("Supplier deleted successfully.");
                    } catch (Exception e) {
                        System.out.println("Error deleting supplier: " + e.getMessage());
                    }
                    break;

                case "5":
                    return;
            }
        }
    }

    private void handleSalesMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Sales Management ---");
            System.out.println("1. Record Product Sale");
            System.out.println("2. List Sales Transactions");
            System.out.println("3. Go Back");
            System.out.print("Select option: ");
            String op = scanner.nextLine().trim();
            switch (op) {
                case "1":
                    try {
                        System.out.print("Enter product ID: ");
                        String prodId = scanner.nextLine().trim();
                        System.out.print("Enter quantity sold: ");
                        int qty = Integer.parseInt(scanner.nextLine().trim());
                        Sale sale = new Sale(null, prodId, qty, null);
                        Sale saved = saleService.recordSale(sale);
                        System.out.println("Sale transaction recorded successfully. ID: " + saved.getId());
                    } catch (Exception e) {
                        System.out.println("Error recording sale: " + e.getMessage());
                    }
                    break;

                case "2":
                    TablePrinter.printSales(saleService.getAllSales());
                    break;

                case "3":
                    return;
            }
        }
    }

    private void handleRemindersMenu() {
        System.out.println("\n--- System Reminders ---");
        TablePrinter.printReminders(reminderService.getAllReminders());
    }

    private void handleAnalyticsMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Analytics Reports ---");
            System.out.println("1. Category-wise Inventory Report");
            System.out.println("2. Monthly Sales Report");
            System.out.println("3. Top Selling Products Report");
            System.out.println("4. Low Stock Report");
            System.out.println("5. Expiring Products Report");
            System.out.println("6. Go Back");
            System.out.print("Select report: ");
            String op = scanner.nextLine().trim();
            switch (op) {
                case "1":
                    System.out.println("\n--- Category-wise Inventory Report ---");
                    TablePrinter.printCategoryWiseInventory(analyticsService.getCategoryWiseInventoryReport());
                    break;

                case "2":
                    System.out.println("\n--- Monthly Sales Report ---");
                    TablePrinter.printMonthlySales(analyticsService.getMonthlySalesReport());
                    break;

                case "3":
                    System.out.println("\n--- Top Selling Products Report ---");
                    TablePrinter.printTopSellingProducts(analyticsService.getTopSellingProductsReport());
                    break;

                case "4":
                    System.out.println("\n--- Low Stock Report ---");
                    TablePrinter.printLowStock(analyticsService.getLowStockReport());
                    break;

                case "5":
                    System.out.println("\n--- Expiring Products Report ---");
                    TablePrinter.printExpiringProducts(analyticsService.getExpiringProductsReport());
                    break;

                case "6":
                    return;
            }
        }
    }
}
