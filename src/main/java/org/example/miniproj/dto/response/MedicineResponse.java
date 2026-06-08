package org.example.miniproj.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.miniproj.entity.Medicine;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MedicineResponse {
    
    private Long id;
    private String name;
    private String description;
    private String manufacturer;
    private BigDecimal price;
    private Integer stockQuantity;
    private String dosageForm;
    private String strength;
    private String activeIngredient;
    private String sideEffects;
    private String contraindications;
    private boolean available;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    public MedicineResponse() {}
    
    public MedicineResponse(Medicine medicine) {
        this.id = medicine.getId();
        this.name = medicine.getName();
        this.description = medicine.getDescription();
        this.manufacturer = medicine.getManufacturer();
        this.price = medicine.getPrice();
        this.stockQuantity = medicine.getStockQuantity();
        this.dosageForm = medicine.getDosageForm();
        this.strength = medicine.getStrength();
        this.activeIngredient = medicine.getActiveIngredient();
        this.sideEffects = medicine.getSideEffects();
        this.contraindications = medicine.getContraindications();
        this.available = medicine.isAvailable();
        this.createdAt = medicine.getCreatedAt();
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public String getDosageForm() { return dosageForm; }
    public void setDosageForm(String dosageForm) { this.dosageForm = dosageForm; }
    
    public String getStrength() { return strength; }
    public void setStrength(String strength) { this.strength = strength; }
    
    public String getActiveIngredient() { return activeIngredient; }
    public void setActiveIngredient(String activeIngredient) { this.activeIngredient = activeIngredient; }
    
    public String getSideEffects() { return sideEffects; }
    public void setSideEffects(String sideEffects) { this.sideEffects = sideEffects; }
    
    public String getContraindications() { return contraindications; }
    public void setContraindications(String contraindications) { this.contraindications = contraindications; }
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}