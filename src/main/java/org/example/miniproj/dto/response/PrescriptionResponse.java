package org.example.miniproj.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.miniproj.entity.Prescription;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PrescriptionResponse {
    
    private Long id;
    private PatientSummaryResponse patient;
    private DoctorSummaryResponse doctor;
    private AppointmentSummaryResponse appointment;
    private List<PrescriptionItemResponse> items;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime prescriptionDate;
    
    private String notes;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    public PrescriptionResponse() {}
    
    public PrescriptionResponse(Prescription prescription) {
        this.id = prescription.getId();
        this.patient = new PatientSummaryResponse(prescription.getPatient());
        this.doctor = new DoctorSummaryResponse(prescription.getDoctor());
        this.appointment = prescription.getAppointment() != null ? 
                         new AppointmentSummaryResponse(prescription.getAppointment()) : null;
        this.items = prescription.getItems().stream()
                    .map(PrescriptionItemResponse::new)
                    .collect(Collectors.toList());
        this.prescriptionDate = prescription.getPrescriptionDate();
        this.notes = prescription.getNotes();
        this.createdAt = prescription.getCreatedAt();
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public PatientSummaryResponse getPatient() { return patient; }
    public void setPatient(PatientSummaryResponse patient) { this.patient = patient; }
    
    public DoctorSummaryResponse getDoctor() { return doctor; }
    public void setDoctor(DoctorSummaryResponse doctor) { this.doctor = doctor; }
    
    public AppointmentSummaryResponse getAppointment() { return appointment; }
    public void setAppointment(AppointmentSummaryResponse appointment) { this.appointment = appointment; }
    
    public List<PrescriptionItemResponse> getItems() { return items; }
    public void setItems(List<PrescriptionItemResponse> items) { this.items = items; }
    
    public LocalDateTime getPrescriptionDate() { return prescriptionDate; }
    public void setPrescriptionDate(LocalDateTime prescriptionDate) { this.prescriptionDate = prescriptionDate; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // Nested response classes
    public static class PatientSummaryResponse {
        private Long id;
        private String fullName;
        private String email;
        
        public PatientSummaryResponse() {}
        
        public PatientSummaryResponse(org.example.miniproj.entity.Patient patient) {
            this.id = patient.getId();
            this.fullName = patient.getFullName();
            this.email = patient.getEmail();
        }
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
    
    public static class DoctorSummaryResponse {
        private Long id;
        private String fullName;
        private String specialization;
        
        public DoctorSummaryResponse() {}
        
        public DoctorSummaryResponse(org.example.miniproj.entity.Doctor doctor) {
            this.id = doctor.getId();
            this.fullName = doctor.getFullName();
            this.specialization = doctor.getSpecialization();
        }
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        
        public String getSpecialization() { return specialization; }
        public void setSpecialization(String specialization) { this.specialization = specialization; }
    }
    
    public static class AppointmentSummaryResponse {
        private Long id;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime appointmentDate;
        
        public AppointmentSummaryResponse() {}
        
        public AppointmentSummaryResponse(org.example.miniproj.entity.Appointment appointment) {
            this.id = appointment.getId();
            this.appointmentDate = appointment.getAppointmentDate();
        }
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public LocalDateTime getAppointmentDate() { return appointmentDate; }
        public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }
    }
    
    public static class PrescriptionItemResponse {
        private Long id;
        private MedicineSummaryResponse medicine;
        private Integer quantity;
        private String dosage;
        private Integer durationDays;
        private String instructions;
        
        public PrescriptionItemResponse() {}
        
        public PrescriptionItemResponse(org.example.miniproj.entity.PrescriptionItem item) {
            this.id = item.getId();
            this.medicine = new MedicineSummaryResponse(item.getMedicine());
            this.quantity = item.getQuantity();
            this.dosage = item.getDosage();
            this.durationDays = item.getDurationDays();
            this.instructions = item.getInstructions();
        }
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public MedicineSummaryResponse getMedicine() { return medicine; }
        public void setMedicine(MedicineSummaryResponse medicine) { this.medicine = medicine; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        
        public String getDosage() { return dosage; }
        public void setDosage(String dosage) { this.dosage = dosage; }
        
        public Integer getDurationDays() { return durationDays; }
        public void setDurationDays(Integer durationDays) { this.durationDays = durationDays; }
        
        public String getInstructions() { return instructions; }
        public void setInstructions(String instructions) { this.instructions = instructions; }
    }
    
    public static class MedicineSummaryResponse {
        private Long id;
        private String name;
        private String strength;
        private String dosageForm;
        
        public MedicineSummaryResponse() {}
        
        public MedicineSummaryResponse(org.example.miniproj.entity.Medicine medicine) {
            this.id = medicine.getId();
            this.name = medicine.getName();
            this.strength = medicine.getStrength();
            this.dosageForm = medicine.getDosageForm();
        }
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getStrength() { return strength; }
        public void setStrength(String strength) { this.strength = strength; }
        
        public String getDosageForm() { return dosageForm; }
        public void setDosageForm(String dosageForm) { this.dosageForm = dosageForm; }
    }
}