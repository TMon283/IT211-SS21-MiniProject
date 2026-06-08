package org.example.miniproj.service;

import org.example.miniproj.dto.request.CreatePatientRequest;
import org.example.miniproj.dto.response.PatientResponse;
import org.example.miniproj.entity.Patient;
import org.example.miniproj.repository.PatientRepository;
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
public class PatientService {
    
    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public PatientResponse createPatient(CreatePatientRequest request) {
        // Check if username already exists
        if (patientRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        
        Patient patient = new Patient();
        patient.setUsername(request.getUsername());
        patient.setPassword(passwordEncoder.encode(request.getPassword()));
        patient.setFullName(request.getFullName());
        patient.setEmail(request.getEmail());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setAddress(request.getAddress());
        patient.setEmergencyContactName(request.getEmergencyContactName());
        patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        patient.setAllergies(request.getAllergies());
        patient.setMedicalHistory(request.getMedicalHistory());
        
        Patient savedPatient = patientRepository.save(patient);
        logger.info("Patient created successfully: {}", savedPatient.getUsername());
        
        return new PatientResponse(savedPatient);
    }
    
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAllActive()
                .map(PatientResponse::new)
                .collect(Collectors.toList());
    }
    
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        return new PatientResponse(patient);
    }
    
    public PatientResponse updatePatient(Long id, CreatePatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        
        // Check if new username conflicts with existing patients (excluding current patient)
        if (!patient.getUsername().equals(request.getUsername()) && 
            patientRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        
        patient.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            patient.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        patient.setFullName(request.getFullName());
        patient.setEmail(request.getEmail());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setAddress(request.getAddress());
        patient.setEmergencyContactName(request.getEmergencyContactName());
        patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        patient.setAllergies(request.getAllergies());
        patient.setMedicalHistory(request.getMedicalHistory());
        
        Patient updatedPatient = patientRepository.save(patient);
        logger.info("Patient updated successfully: {}", updatedPatient.getUsername());
        
        return new PatientResponse(updatedPatient);
    }
    
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
        
        patient.setEnabled(false);
        patientRepository.save(patient);
        
        logger.info("Patient deactivated successfully: {}", patient.getUsername());
    }
}