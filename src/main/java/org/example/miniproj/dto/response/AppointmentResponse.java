package org.example.miniproj.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.miniproj.entity.Appointment;

import java.time.LocalDateTime;

public class AppointmentResponse {
    
    private Long id;
    private PatientSummaryResponse patient;
    private DoctorSummaryResponse doctor;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentDate;
    
    private String status;
    private String notes;
    private String diagnosis;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    public AppointmentResponse() {}
    
    public AppointmentResponse(Appointment appointment) {
        this.id = appointment.getId();
        this.patient = new PatientSummaryResponse(appointment.getPatient());
        this.doctor = new DoctorSummaryResponse(appointment.getDoctor());
        this.appointmentDate = appointment.getAppointmentDate();
        this.status = appointment.getStatus().name();
        this.notes = appointment.getNotes();
        this.diagnosis = appointment.getDiagnosis();
        this.createdAt = appointment.getCreatedAt();
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public PatientSummaryResponse getPatient() { return patient; }
    public void setPatient(PatientSummaryResponse patient) { this.patient = patient; }
    
    public DoctorSummaryResponse getDoctor() { return doctor; }
    public void setDoctor(DoctorSummaryResponse doctor) { this.doctor = doctor; }
    
    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    // Nested response classes
    public static class PatientSummaryResponse {
        private Long id;
        private String fullName;
        private String email;
        private String phoneNumber;
        
        public PatientSummaryResponse() {}
        
        public PatientSummaryResponse(org.example.miniproj.entity.Patient patient) {
            this.id = patient.getId();
            this.fullName = patient.getFullName();
            this.email = patient.getEmail();
            this.phoneNumber = patient.getPhoneNumber();
        }
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    }
    
    public static class DoctorSummaryResponse {
        private Long id;
        private String fullName;
        private String specialization;
        
        public DoctorSummaryResponse() {}
        
        public DoctorSummaryResponse(org.example.miniproj.entity.Doctor doctor) {
            this.id = doctor.getId();
            this.fullName = doctor.getFullName();
            this.specialization = doctor.getSpecialization();
        }
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        
        public String getSpecialization() { return specialization; }
        public void setSpecialization(String specialization) { this.specialization = specialization; }
    }
}