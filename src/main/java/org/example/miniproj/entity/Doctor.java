package org.example.miniproj.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Doctor extends User {
    
    @NotBlank
    private String specialization;
    
    private String licenseNumber;
    
    private Integer experienceYears;
    
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();
    
    // Custom constructor for doctor creation
    public Doctor(String username, String password, String fullName, String email, 
                  String specialization, String licenseNumber) {
        super(username, password, fullName, email, Role.DOCTOR);
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
    }
}