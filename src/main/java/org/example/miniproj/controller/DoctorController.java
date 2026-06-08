package org.example.miniproj.controller;

import jakarta.validation.Valid;
import org.example.miniproj.dto.request.CreatePrescriptionRequest;
import org.example.miniproj.dto.response.ApiResponse;
import org.example.miniproj.dto.response.AppointmentResponse;
import org.example.miniproj.dto.response.MedicineResponse;
import org.example.miniproj.dto.response.PrescriptionResponse;
import org.example.miniproj.service.AppointmentService;
import org.example.miniproj.service.MedicineService;
import org.example.miniproj.service.PrescriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@PreAuthorize("hasRole('DOCTOR')")
public class DoctorController {
    
    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private PrescriptionService prescriptionService;
    
    @Autowired
    private MedicineService medicineService;
    
    // Appointment Management
    @GetMapping("/appointments/today")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getTodayAppointments() {
        List<AppointmentResponse> appointments = appointmentService.getDoctorTodayAppointments();
        return ResponseEntity.ok(ApiResponse.success("Today's appointments retrieved successfully", appointments));
    }
    
    @GetMapping("/appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime date) {
        List<AppointmentResponse> appointments = appointmentService.getDoctorAppointments(date);
        return ResponseEntity.ok(ApiResponse.success("Appointments retrieved successfully", appointments));
    }
    
    @PutMapping("/appointments/{id}/status")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String diagnosis) {
        try {
            AppointmentResponse response = appointmentService.updateAppointmentStatus(id, status, diagnosis);
            return ResponseEntity.ok(ApiResponse.success("Appointment status updated successfully", response));
        } catch (Exception e) {
            logger.error("Failed to update appointment status", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
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
    
    // Prescription Management
    @PostMapping("/prescriptions")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> createPrescription(@Valid @RequestBody CreatePrescriptionRequest request) {
        try {
            PrescriptionResponse response = prescriptionService.createPrescription(request);
            return ResponseEntity.ok(ApiResponse.success("Prescription created successfully", response));
        } catch (Exception e) {
            logger.error("Failed to create prescription", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/prescriptions")
    public ResponseEntity<ApiResponse<List<PrescriptionResponse>>> getMyPrescriptions() {
        List<PrescriptionResponse> prescriptions = prescriptionService.getDoctorPrescriptions();
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
    
    // Medicine Information (for creating prescriptions)
    @GetMapping("/medicines")
    public ResponseEntity<ApiResponse<List<MedicineResponse>>> getAllMedicines() {
        List<MedicineResponse> medicines = medicineService.getAllMedicines();
        return ResponseEntity.ok(ApiResponse.success("Medicines retrieved successfully", medicines));
    }
    
    @GetMapping("/medicines/search")
    public ResponseEntity<ApiResponse<List<MedicineResponse>>> searchMedicines(@RequestParam String name) {
        List<MedicineResponse> medicines = medicineService.searchMedicinesByName(name);
        return ResponseEntity.ok(ApiResponse.success("Medicines retrieved successfully", medicines));
    }
    
    @GetMapping("/medicines/in-stock")
    public ResponseEntity<ApiResponse<List<MedicineResponse>>> getMedicinesInStock() {
        List<MedicineResponse> medicines = medicineService.getMedicinesInStock();
        return ResponseEntity.ok(ApiResponse.success("In-stock medicines retrieved successfully", medicines));
    }
}