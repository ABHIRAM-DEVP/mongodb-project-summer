package com.SmartStockPro.app.service;

import com.SmartStockPro.app.model.Supplier;
import com.SmartStockPro.app.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
    public Optional<Supplier> getSupplierById(String id) {
        return supplierRepository.findById(id);
    }
    public Supplier updateSupplier(String id, Supplier supplierDetails) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Supplier not found: " + id));
        supplier.setSupplierName(supplierDetails.getSupplierName());
        supplier.setContact(supplierDetails.getContact());
        return supplierRepository.save(supplier);
    }
    public void deleteSupplier(String id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Supplier not found: " + id));
        supplierRepository.delete(supplier);
    }
}
