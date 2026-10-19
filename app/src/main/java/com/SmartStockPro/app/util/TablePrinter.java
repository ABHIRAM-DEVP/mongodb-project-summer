package com.SmartStockPro.app.util;

import com.SmartStockPro.app.model.*;
import java.util.ArrayList;
import java.util.List;

public class TablePrinter {

    public static void printTable(String[] headers, List<String[]> rows) {
        if (headers == null || headers.length == 0) return;

        int numCols = headers.length;
        int[] colWidths = new int[numCols];

        // Initialize with header lengths
        for (int i = 0; i < numCols; i++) {
            colWidths[i] = headers[i].length();
        }

        // Calculate max widths based on content
        for (String[] row : rows) {
            for (int i = 0; i < numCols && i < row.length; i++) {
                if (row[i] != null && row[i].length() > colWidths[i]) {
                    colWidths[i] = row[i].length();
                }
            }
        }

        // Build line separator
        StringBuilder borderBuilder = new StringBuilder();
        borderBuilder.append("+");
        for (int i = 0; i < numCols; i++) {
            borderBuilder.append("-".repeat(colWidths[i] + 2));
            borderBuilder.append("+");
        }
        String border = borderBuilder.toString();

        // Header print
        System.out.println(border);
        System.out.print("|");
        for (int i = 0; i < numCols; i++) {
            System.out.printf(" %-" + colWidths[i] + "s |", headers[i]);
        }
        System.out.println();
        System.out.println(border);

        // Row print
        if (rows.isEmpty()) {
            System.out.print("|");
            int innerWidth = border.length() - 2;
            String noData = "No records found.";
            if (noData.length() > innerWidth) {
                noData = noData.substring(0, innerWidth);
            }
            int padding = (innerWidth - noData.length()) / 2;
            System.out.printf("%s%s%s|\n", " ".repeat(padding), noData, " ".repeat(innerWidth - noData.length() - padding));
        } else {
            for (String[] row : rows) {
                System.out.print("|");
                for (int i = 0; i < numCols; i++) {
                    String val = (i < row.length && row[i] != null) ? row[i] : "";
                    System.out.printf(" %-" + colWidths[i] + "s |", val);
                }
                System.out.println();
            }
        }
        System.out.println(border);
    }

    public static void printProducts(List<Product> products) {
        String[] headers = {"Product ID", "Product Name", "Price", "Qty", "Threshold", "Expiry", "Warehouse"};
        List<String[]> rows = new ArrayList<>();
        for (Product p : products) {
            String expDate = p.getExpiryDate() != null ? p.getExpiryDate().toString() : "N/A";
            String whLocation = p.getInventoryDetails() != null ? p.getInventoryDetails().getWarehouseLocation() : "N/A";
            rows.add(new String[]{
                p.getId() != null ? p.getId() : "N/A",
                p.getName() != null ? p.getName() : "N/A",
                String.format("%.2f", p.getPrice()),
                String.valueOf(p.getQuantity()),
                String.valueOf(p.getThreshold()),
                expDate,
                whLocation
            });
        }
        printTable(headers, rows);
    }

    public static void printCategories(List<Category> categories) {
        String[] headers = {"Category ID", "Category Name"};
        List<String[]> rows = new ArrayList<>();
        for (Category c : categories) {
            rows.add(new String[]{
                c.getId() != null ? c.getId() : "N/A",
                c.getCategoryName() != null ? c.getCategoryName() : "N/A"
            });
        }
        printTable(headers, rows);
    }

    public static void printSuppliers(List<Supplier> suppliers) {
        String[] headers = {"Supplier ID", "Supplier Name", "Contact Details"};
        List<String[]> rows = new ArrayList<>();
        for (Supplier s : suppliers) {
            rows.add(new String[]{
                s.getId() != null ? s.getId() : "N/A",
                s.getSupplierName() != null ? s.getSupplierName() : "N/A",
                s.getContact() != null ? s.getContact() : "N/A"
            });
        }
        printTable(headers, rows);
    }

    public static void printSales(List<Sale> sales) {
        String[] headers = {"Transaction ID", "Product ID", "Quantity Sold", "Sale Date"};
        List<String[]> rows = new ArrayList<>();
        for (Sale s : sales) {
            rows.add(new String[]{
                s.getId() != null ? s.getId() : "N/A",
                s.getProductId() != null ? s.getProductId() : "N/A",
                String.valueOf(s.getQuantitySold()),
                s.getSaleDate() != null ? s.getSaleDate().toString() : "N/A"
            });
        }
        printTable(headers, rows);
    }

    public static void printCategoryWiseInventory(List<org.bson.Document> report) {
        String[] headers = {"Category ID", "Category Name", "Product Type Count", "Total Items Stocked"};
        List<String[]> rows = new ArrayList<>();
        for (org.bson.Document doc : report) {
            rows.add(new String[]{
                String.valueOf(doc.get("categoryId")),
                String.valueOf(doc.get("categoryName")),
                String.valueOf(doc.get("productCount")),
                String.valueOf(doc.get("totalQuantity"))
            });
        }
        printTable(headers, rows);
    }

    public static void printMonthlySales(List<org.bson.Document> report) {
        String[] headers = {"Year / Month", "Total Sold Quantity", "Transaction Count"};
        List<String[]> rows = new ArrayList<>();
        for (org.bson.Document doc : report) {
            org.bson.Document idDoc = (org.bson.Document) doc.get("_id");
            String dateLabel = "N/A";
            if (idDoc != null) {
                dateLabel = String.format("%d / %02d", idDoc.getInteger("year"), idDoc.getInteger("month"));
            }
            rows.add(new String[]{
                dateLabel,
                String.valueOf(doc.get("totalQuantitySold")),
                String.valueOf(doc.get("transactionCount"))
            });
        }
        printTable(headers, rows);
    }

    public static void printTopSellingProducts(List<org.bson.Document> report) {
        String[] headers = {"Product ID", "Product Name", "Total Volume Sold"};
        List<String[]> rows = new ArrayList<>();
        for (org.bson.Document doc : report) {
            rows.add(new String[]{
                String.valueOf(doc.get("productId")),
                String.valueOf(doc.get("productName")),
                String.valueOf(doc.get("totalQuantitySold"))
            });
        }
        printTable(headers, rows);
    }

    public static void printLowStock(List<org.bson.Document> report) {
        String[] headers = {"Product Name", "Category Name", "Current Qty", "Threshold Qty"};
        List<String[]> rows = new ArrayList<>();
        for (org.bson.Document doc : report) {
            rows.add(new String[]{
                String.valueOf(doc.get("name")),
                String.valueOf(doc.get("categoryName")),
                String.valueOf(doc.get("quantity")),
                String.valueOf(doc.get("threshold"))
            });
        }
        printTable(headers, rows);
    }

    public static void printExpiringProducts(List<org.bson.Document> report) {
        String[] headers = {"Product Name", "Category Name", "Current Qty", "Expiry Date"};
        List<String[]> rows = new ArrayList<>();
        for (org.bson.Document doc : report) {
            rows.add(new String[]{
                String.valueOf(doc.get("name")),
                String.valueOf(doc.get("categoryName")),
                String.valueOf(doc.get("quantity")),
                String.valueOf(doc.get("expiryDate"))
            });
        }
        printTable(headers, rows);
    }

    public static void printReminders(List<Reminder> reminders) {
        String[] headers = {"Logged Time", "Notification Alert Message"};
        List<String[]> rows = new ArrayList<>();
        for (Reminder r : reminders) {
            rows.add(new String[]{
                r.getCreatedAt() != null ? r.getCreatedAt().toString() : "N/A",
                r.getMessage() != null ? r.getMessage() : "N/A"
            });
        }
        printTable(headers, rows);
    }
}
