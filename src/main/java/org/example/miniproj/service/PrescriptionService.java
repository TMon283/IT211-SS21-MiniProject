package org.example.miniproj.service;

import org.example.miniproj.dto.request.CreatePrescriptionRequest;
import org.example.miniproj.dto.response.PrescriptionResponse;
import org.example.miniproj.entity.*;
import org.example.miniproj.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrescriptionService {
    
    private static final Logger logger = LoggerFactory.getLogger(PrescriptionService.class);
    
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private MedicineRepository medicineRepository;
    
    public PrescriptionResponse createPrescription(CreatePrescriptionRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        if (currentUser.getRole() != User.Role.DOCTOR) {
            throw new RuntimeException("Only doctors can create prescriptions");
        }
        
        Doctor doctor = doctorRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + request.getPatientId()));
        
        Appointment appointment = null;
        if (request.getAppointmentId() != null) {
            appointment = appointmentRepository.findById(request.getAppointmentId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + request.getAppointmentId()));
            
            // Verify that the appointment belongs to the current doctor and patient
            if (!appointment.getDoctor().getId().equals(doctor.getId()) ||
                !appointment.getPatient().getId().equals(patient.getId())) {
                throw new RuntimeException("Invalid appointment for this doctor and patient");
            }
        }
        
        Prescription prescription = new Prescription();
        prescription.setPatient(patient);
        prescription.setDoctor(doctor);
        prescription.setAppointment(appointment);
        prescription.setNotes(request.getNotes());
        
        Prescription savedPrescription = prescriptionRepository.save(prescription);
        
        // Create prescription items
        for (CreatePrescriptionRequest.PrescriptionItemRequest itemRequest : request.getItems()) {
            Medicine medicine = medicineRepository.findById(itemRequest.getMedicineId())
                    .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + itemRequest.getMedicineId()));
            
            if (!medicine.isAvailable()) {
                throw new RuntimeException("Medicine is not available: " + medicine.getName());
            }
            
            // Note: In a real system, you might want to check stock and update it here
            
            PrescriptionItem item = new PrescriptionItem();
            item.setPrescription(savedPrescription);
            item.setMedicine(medicine);
            item.setQuantity(itemRequest.getQuantity());
            item.setDosage(itemRequest.getDosage());
            item.setDurationDays(itemRequest.getDurationDays());
            item.setInstructions(itemRequest.getInstructions());
            
            savedPrescription.getItems().add(item);
        }
        
        prescriptionRepository.save(savedPrescription);
        
        logger.info("Prescription created successfully by doctor: {} for patient: {}", 
                   doctor.getUsername(), patient.getUsername());
        
        return new PrescriptionResponse(savedPrescription);
    }
    
    public List<PrescriptionResponse> getPatientPrescriptions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        Patient patient = patientRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        return prescriptionRepository.findByPatientOrderByPrescriptionDateDesc(patient)
                .map(PrescriptionResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<PrescriptionResponse> getDoctorPrescriptions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        Doctor doctor = doctorRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        return prescriptionRepository.findByDoctorOrderByPrescriptionDateDesc(doctor)
                .map(PrescriptionResponse::new)
                .collect(Collectors.toList());
    }
    
    public PrescriptionResponse getPrescriptionById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        Prescription prescription = prescriptionRepository.findByIdWithItems(id);
        if (prescription == null) {
            throw new RuntimeException("Prescription not found with id: " + id);
        }
        
        // Check authorization
        boolean isAuthorized = false;
        if (currentUser.getRole() == User.Role.PATIENT) {
            isAuthorized = prescription.getPatient().getId().equals(currentUser.getId());
        } else if (currentUser.getRole() == User.Role.DOCTOR) {
            isAuthorized = prescription.getDoctor().getId().equals(currentUser.getId());
        } else if (currentUser.getRole() == User.Role.ADMIN) {
            isAuthorized = true;
        }
        
        if (!isAuthorized) {
            throw new RuntimeException("You are not authorized to view this prescription");
        }
        
        return new PrescriptionResponse(prescription);
    }
}