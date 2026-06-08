package org.example.miniproj.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreatePrescriptionRequest {
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    private Long appointmentId;
    
    @NotEmpty(message = "Prescription items cannot be empty")
    @Valid
    private List<PrescriptionItemRequest> items;
    
    private String notes;
    
    public CreatePrescriptionRequest() {}
    
    // Getters and setters
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    
    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    
    public List<PrescriptionItemRequest> getItems() { return items; }
    public void setItems(List<PrescriptionItemRequest> items) { this.items = items; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public static class PrescriptionItemRequest {
        
        @NotNull(message = "Medicine ID is required")
        private Long medicineId;
        
        @NotNull(message = "Quantity is required")
        private Integer quantity;
        
        @NotNull(message = "Dosage is required")
        private String dosage;
        
        private Integer durationDays;
        
        private String instructions;
        
        public PrescriptionItemRequest() {}
        
        // Getters and setters
        public Long getMedicineId() { return medicineId; }
        public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        
        public String getDosage() { return dosage; }
        public void setDosage(String dosage) { this.dosage = dosage; }
        
        public Integer getDurationDays() { return durationDays; }
        public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }
        
        public String getInstructions() { return instructions; }
        public void setInstructions(String instructions) { this.instructions = instructions; }
    }
}