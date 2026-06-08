package org.example.miniproj.repository;

import org.example.miniproj.entity.Appointment;
import org.example.miniproj.entity.Doctor;
import org.example.miniproj.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor = :doctor AND DATE(a.appointmentDate) = DATE(:date)")
    Stream<Appointment> findByDoctorAndDate(@Param("doctor") Doctor doctor, @Param("date") LocalDateTime date);
    
    @Query("SELECT a FROM Appointment a WHERE a.patient = :patient ORDER BY a.appointmentDate DESC")
    Stream<Appointment> findByPatientOrderByAppointmentDateDesc(@Param("patient") Patient patient);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor = :doctor AND a.appointmentDate BETWEEN :startDate AND :endDate")
    Stream<Appointment> findByDoctorAndAppointmentDateBetween(@Param("doctor") Doctor doctor, 
                                                               @Param("startDate") LocalDateTime startDate, 
                                                               @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT a FROM Appointment a WHERE a.status = :status")
    Stream<Appointment> findByStatus(@Param("status") Appointment.Status status);
    
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.doctor = :doctor AND a.appointmentDate = :appointmentDate AND a.status != 'CANCELLED'")
    boolean existsByDoctorAndAppointmentDateAndStatusNot(@Param("doctor") Doctor doctor, 
                                                         @Param("appointmentDate") LocalDateTime appointmentDate);
}