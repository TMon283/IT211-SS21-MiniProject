package org.example.miniproj.service;

import org.example.miniproj.dto.request.CreateAppointmentRequest;
import org.example.miniproj.dto.response.AppointmentResponse;
import org.example.miniproj.entity.Appointment;
import org.example.miniproj.entity.Doctor;
import org.example.miniproj.entity.Patient;
import org.example.miniproj.entity.User;
import org.example.miniproj.repository.AppointmentRepository;
import org.example.miniproj.repository.DoctorRepository;
import org.example.miniproj.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {
    
    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    public AppointmentResponse createAppointment(CreateAppointmentRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        if (currentUser.getRole() != User.Role.PATIENT) {
            throw new RuntimeException("Only patients can create appointments");
        }
        
        Patient patient = patientRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + request.getDoctorId()));
        
        // Check if the doctor is available at the requested time
        if (appointmentRepository.existsByDoctorAndAppointmentDateAndStatusNot(
                doctor, request.getAppointmentDate())) {
            throw new RuntimeException("Doctor is not available at the requested time");
        }
        
        // Check if appointment date is in the future
        if (request.getAppointmentDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Appointment date must be in the future");
        }
        
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setNotes(request.getNotes());
        appointment.setStatus(Appointment.Status.SCHEDULED);
        
        Appointment savedAppointment = appointmentRepository.save(appointment);
        logger.info("Appointment created successfully for patient: {} with doctor: {}", 
                   patient.getUsername(), doctor.getUsername());
        
        return new AppointmentResponse(savedAppointment);
    }
    
    public List<AppointmentResponse> getPatientAppointments() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        Patient patient = patientRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        return appointmentRepository.findByPatientOrderByAppointmentDateDesc(patient)
                .map(AppointmentResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<AppointmentResponse> getDoctorAppointments(LocalDateTime date) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        Doctor doctor = doctorRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);
        
        return appointmentRepository.findByDoctorAndAppointmentDateBetween(doctor, startOfDay, endOfDay)
                .map(AppointmentResponse::new)
                .collect(Collectors.toList());
    }
    
    public List<AppointmentResponse> getDoctorTodayAppointments() {
        return getDoctorAppointments(LocalDateTime.now());
    }
    
    public AppointmentResponse updateAppointmentStatus(Long appointmentId, String status, String diagnosis) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));
        
        // Check if the current user is the doctor for this appointment
        if (!appointment.getDoctor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only update your own appointments");
        }
        
        try {
            Appointment.Status newStatus = Appointment.Status.valueOf(status.toUpperCase());
            appointment.setStatus(newStatus);
            if (diagnosis != null && !diagnosis.isEmpty()) {
                appointment.setDiagnosis(diagnosis);
            }
            
            Appointment updatedAppointment = appointmentRepository.save(appointment);
            logger.info("Appointment status updated to {} for appointment id: {}", newStatus, appointmentId);
            
            return new AppointmentResponse(updatedAppointment);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid appointment status: " + status);
        }
    }
    
    public void cancelAppointment(Long appointmentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));
        
        // Check if the current user is either the patient or doctor for this appointment
        boolean isAuthorized = appointment.getPatient().getId().equals(currentUser.getId()) ||
                              appointment.getDoctor().getId().equals(currentUser.getId());
        
        if (!isAuthorized) {
            throw new RuntimeException("You can only cancel your own appointments");
        }
        
        appointment.setStatus(Appointment.Status.CANCELLED);
        appointmentRepository.save(appointment);
        
        logger.info("Appointment cancelled for appointment id: {}", appointmentId);
    }
}