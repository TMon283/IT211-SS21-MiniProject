package org.example.miniproj.service;

import org.example.miniproj.dto.request.CreateDoctorRequest;
import org.example.miniproj.dto.response.DoctorResponse;
import org.example.miniproj.entity.Doctor;
import org.example.miniproj.repository.DoctorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DoctorService {
    
    private static final Logger logger = LoggerFactory.getLogger(DoctorService.class);
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public DoctorResponse createDoctor(CreateDoctorRequest request) {
        // Check if username, email, or license number already exists
        if (doctorRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        
        if (doctorRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new RuntimeException("License number already exists");
        }
        
        Doctor doctor = new Doctor();
        doctor.setUsername(request.getUsername());
        doctor.setPassword(passwordEncoder.encode(request.getPassword()));
        doctor.setFullName(request.getFullName());
        doctor.setEmail(request.getEmail());
        doctor.setPhoneNumber(request.getPhoneNumber());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setExperienceYears(request.getExperienceYears());
        
        Doctor savedDoctor = doctorRepository.save(doctor);
        logger.info("Doctor created successfully: {}", savedDoctor.getUsername());
        
        return new DoctorResponse(savedDoctor);
    }
    
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAllActive()
                .map(DoctorResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<DoctorResponse> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationAndEnabled(specialization)
                .map(DoctorResponse::new)
                .collect(Collectors.toList());
    }
    
    public DoctorResponse getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        return new DoctorResponse(doctor);
    }
    
    public DoctorResponse updateDoctor(Long id, CreateDoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        
        // Check if new username/license conflicts with existing doctors (excluding current doctor)
        if (!doctor.getUsername().equals(request.getUsername()) && 
            doctorRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        
        if (!doctor.getLicenseNumber().equals(request.getLicenseNumber()) && 
            doctorRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new RuntimeException("License number already exists");
        }
        
        doctor.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            doctor.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        doctor.setFullName(request.getFullName());
        doctor.setEmail(request.getEmail());
        doctor.setPhoneNumber(request.getPhoneNumber());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setExperienceYears(request.getExperienceYears());
        
        Doctor updatedDoctor = doctorRepository.save(doctor);
        logger.info("Doctor updated successfully: {}", updatedDoctor.getUsername());
        
        return new DoctorResponse(updatedDoctor);
    }
    
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        
        doctor.setEnabled(false);
        doctorRepository.save(doctor);
        
        logger.info("Doctor deactivated successfully: {}", doctor.getUsername());
    }
}