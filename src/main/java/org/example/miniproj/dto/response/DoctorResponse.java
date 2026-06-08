package org.example.miniproj.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.miniproj.entity.Doctor;

import java.time.LocalDateTime;

public class DoctorResponse {
    
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String specialization;
    private String licenseNumber;
    private Integer experienceYears;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    public DoctorResponse() {}
    
    public DoctorResponse(Doctor doctor) {
        this.id = doctor.getId();
        this.username = doctor.getUsername();
        this.fullName = doctor.getFullName();
        this.email = doctor.getEmail();
        this.phoneNumber = doctor.getPhoneNumber();
        this.specialization = doctor.getSpecialization();
        this.licenseNumber = doctor.getLicenseNumber();
        this.experienceYears = doctor.getExperienceYears();
        this.createdAt = doctor.getCreatedAt();
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
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}