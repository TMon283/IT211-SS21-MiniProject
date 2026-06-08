package org.example.miniproj.repository;

import org.example.miniproj.entity.Prescription;
import org.example.miniproj.entity.Patient;
import org.example.miniproj.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    
    @Query("SELECT p FROM Prescription p WHERE p.patient = :patient ORDER BY p.prescriptionDate DESC")
    Stream<Prescription> findByPatientOrderByPrescriptionDateDesc(@Param("patient") Patient patient);
    
    @Query("SELECT p FROM Prescription p WHERE p.doctor = :doctor ORDER BY p.prescriptionDate DESC")
    Stream<Prescription> findByDoctorOrderByPrescriptionDateDesc(@Param("doctor") Doctor doctor);
    
    @Query("SELECT p FROM Prescription p JOIN FETCH p.items pi JOIN FETCH pi.medicine WHERE p.id = :id")
    Prescription findByIdWithItems(@Param("id") Long id);
}