package org.example.miniproj.repository;

import org.example.miniproj.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    Optional<Patient> findByUsername(String username);
    
    @Query("SELECT p FROM Patient p WHERE p.enabled = true")
    Stream<Patient> findAllActive();
    
    @Query("SELECT p FROM Patient p WHERE p.emergencyContactPhone = :phone")
    Stream<Patient> findByEmergencyContactPhone(@Param("phone") String phone);
}