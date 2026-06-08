package org.example.miniproj.controller;

import jakarta.validation.Valid;
import org.example.miniproj.dto.request.CreateAppointmentRequest;
import org.example.miniproj.dto.response.ApiResponse;
import org.example.miniproj.dto.response.AppointmentResponse;
import org.example.miniproj.dto.response.DoctorResponse;
import org.example.miniproj.dto.response.PrescriptionResponse;
import org.example.miniproj.service.AppointmentService;
import org.example.miniproj.service.DoctorService;
import org.example.miniproj.service.PrescriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@PreAuthorize("hasRole('PATIENT')")
public class PatientController {
    
    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private PrescriptionService prescriptionService;
    
    // Appointment Management
    @PostMapping("/appointments")
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(@Valid @RequestBody CreateAppointmentRequest request) {
        try {
            AppointmentResponse response = appointmentService.createAppointment(request);
            return ResponseEntity.ok(ApiResponse.success("Appointment created successfully", response));
        } catch (Exception e) {
            logger.error("Failed to create appointment", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getMyAppointments() {
        List<AppointmentResponse> appointments = appointmentService.getPatientAppointments();
        return ResponseEntity.ok(ApiResponse.success("Appointments retrieved successfully", appointments));
    }
    
    @PutMapping("/appointments/{id}/cancel")
    public ResponseEntity<ApiResponse<String>> cancelAppointment(@PathVariable Long id) {
        try {
            appointmentService.cancelAppointment(id);
            return ResponseEntity.ok(ApiResponse.success("Appointment cancelled successfully"));
        } catch (Exception e) {
            logger.error("Failed to cancel appointment", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // Doctor Information (for booking appointments)
    @GetMapping("/doctors")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAllDoctors() {
        List<DoctorResponse> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(ApiResponse.success("Doctors retrieved successfully", doctors));
    }
    
    @GetMapping("/doctors/specialization/{specialization}")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getDoctorsBySpecialization(@PathVariable String specialization) {
        List<DoctorResponse> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(ApiResponse.success("Doctors retrieved successfully", doctors));
    }
    
    // Prescription Management
    @GetMapping("/prescriptions")
    public ResponseEntity<ApiResponse<List<PrescriptionResponse>>> getMyPrescriptions() {
        List<PrescriptionResponse> prescriptions = prescriptionService.getPatientPrescriptions();
        return ResponseEntity.ok(ApiResponse.success("Prescriptions retrieved successfully", prescriptions));
    }
    
    @GetMapping("/prescriptions/{id}")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> getPrescriptionById(@PathVariable Long id) {
        try {
            PrescriptionResponse prescription = prescriptionService.getPrescriptionById(id);
            return ResponseEntity.ok(ApiResponse.success("Prescription retrieved successfully", prescription));
        } catch (Exception e) {
            logger.error("Failed to retrieve prescription", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}