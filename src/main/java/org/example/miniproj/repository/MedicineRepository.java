package org.example.miniproj.repository;

import org.example.miniproj.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    
    @Query("SELECT m FROM Medicine m WHERE m.available = true")
    Stream<Medicine> findAvailableMedicines();
    
    @Query("SELECT m FROM Medicine m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')) AND m.available = true")
    Stream<Medicine> findByNameContainingIgnoreCaseAndAvailable(@Param("name") String name);
    
    @Query("SELECT m FROM Medicine m WHERE m.manufacturer = :manufacturer AND m.available = true")
    Stream<Medicine> findByManufacturerAndAvailable(@Param("manufacturer") String manufacturer);
    
    @Query("SELECT m FROM Medicine m WHERE m.stockQuantity > 0 AND m.available = true")
    Stream<Medicine> findInStockMedicines();
    
    boolean existsByName(String name);
}