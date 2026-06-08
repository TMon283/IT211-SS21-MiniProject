package org.example.miniproj.repository;

import org.example.miniproj.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    Optional<Doctor> findByUsername(String username);
    
    @Query("SELECT d FROM Doctor d WHERE d.specialization = :specialization AND d.enabled = true")
    Stream<Doctor> findBySpecializationAndEnabled(@Param("specialization") String specialization);
    
    @Query("SELECT d FROM Doctor d WHERE d.enabled = true")
    Stream<Doctor> findAllActive();
    
    boolean existsByLicenseNumber(String licenseNumber);
}