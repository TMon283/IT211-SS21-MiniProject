package org.example.miniproj.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends User {
    
    private LocalDate dateOfBirth;
    
    private String address;
    
    private String emergencyContactName;
    
    private String emergencyContactPhone;
    
    private String allergies;
    
    private String medicalHistory;
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prescription> prescriptions = new ArrayList<>();
    
    // Custom constructor for patient creation
    public Patient(String username, String password, String fullName, String email, 
                   LocalDate dateOfBirth, String address) {
        super(username, password, fullName, email, Role.PATIENT);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}