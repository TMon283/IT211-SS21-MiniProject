package org.example.miniproj.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.miniproj.entity.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientResponse {
    
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    
    private String address;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String allergies;
    private String medicalHistory;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    public PatientResponse() {}
    
    public PatientResponse(Patient patient) {
        this.id = patient.getId();
        this.username = patient.getUsername();
        this.fullName = patient.getFullName();
        this.email = patient.getEmail();
        this.phoneNumber = patient.getPhoneNumber();
        this.dateOfBirth = patient.getDateOfBirth();
        this.address = patient.getAddress();
        this.emergencyContactName = patient.getEmergencyContactName();
        this.emergencyContactPhone = patient.getEmergencyContactPhone();
        this.allergies = patient.getAllergies();
        this.medicalHistory = patient.getMedicalHistory();
        this.createdAt = patient.getCreatedAt();
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getEmergencyContactName() { return emergencyContactName; }
    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }
    
    public String getEmergencyContactPhone() { return emergencyContactPhone; }
    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }
    
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    
    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}