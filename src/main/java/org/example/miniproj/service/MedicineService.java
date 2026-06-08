package org.example.miniproj.service;

import org.example.miniproj.dto.request.CreateMedicineRequest;
import org.example.miniproj.dto.response.MedicineResponse;
import org.example.miniproj.entity.Medicine;
import org.example.miniproj.repository.MedicineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MedicineService {
    
    private static final Logger logger = LoggerFactory.getLogger(MedicineService.class);
    
    @Autowired
    private MedicineRepository medicineRepository;
    
    public MedicineResponse createMedicine(CreateMedicineRequest request) {
        // Check if medicine name already exists
        if (medicineRepository.existsByName(request.getName())) {
            throw new RuntimeException("Medicine name already exists");
        }
        
        Medicine medicine = new Medicine();
        medicine.setName(request.getName());
        medicine.setDescription(request.getDescription());
        medicine.setManufacturer(request.getManufacturer());
        medicine.setPrice(request.getPrice());
        medicine.setStockQuantity(request.getStockQuantity());
        medicine.setDosageForm(request.getDosageForm());
        medicine.setStrength(request.getStrength());
        medicine.setActiveIngredient(request.getActiveIngredient());
        medicine.setSideEffects(request.getSideEffects());
        medicine.setContraindications(request.getContraindications());
        
        Medicine savedMedicine = medicineRepository.save(medicine);
        logger.info("Medicine created successfully: {}", savedMedicine.getName());
        
        return new MedicineResponse(savedMedicine);
    }
    
    public List<MedicineResponse> getAllMedicines() {
        return medicineRepository.findAvailableMedicines()
                .map(MedicineResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<MedicineResponse> getMedicinesInStock() {
        return medicineRepository.findInStockMedicines()
                .map(MedicineResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<MedicineResponse> searchMedicinesByName(String name) {
        return medicineRepository.findByNameContainingIgnoreCaseAndAvailable(name)
                .map(MedicineResponse::new)
                .collect(Collectors.toList());
    }
    
    public MedicineResponse getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
        return new MedicineResponse(medicine);
    }
    
    public MedicineResponse updateMedicine(Long id, CreateMedicineRequest request) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
        
        // Check if new name conflicts with existing medicines (excluding current medicine)
        if (!medicine.getName().equals(request.getName()) && 
            medicineRepository.existsByName(request.getName())) {
            throw new RuntimeException("Medicine name already exists");
        }
        
        medicine.setName(request.getName());
        medicine.setDescription(request.getDescription());
        medicine.setManufacturer(request.getManufacturer());
        medicine.setPrice(request.getPrice());
        medicine.setStockQuantity(request.getStockQuantity());
        medicine.setDosageForm(request.getDosageForm());
        medicine.setStrength(request.getStrength());
        medicine.setActiveIngredient(request.getActiveIngredient());
        medicine.setSideEffects(request.getSideEffects());
        medicine.setContraindications(request.getContraindications());
        
        Medicine updatedMedicine = medicineRepository.save(medicine);
        logger.info("Medicine updated successfully: {}", updatedMedicine.getName());
        
        return new MedicineResponse(updatedMedicine);
    }
    
    public void deleteMedicine(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
        
        medicine.setAvailable(false);
        medicineRepository.save(medicine);
        
        logger.info("Medicine deactivated successfully: {}", medicine.getName());
    }
}