package org.example.miniproj.config;

import org.example.miniproj.entity.*;
import org.example.miniproj.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private MedicineRepository medicineRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            initializeData();
            logger.info("Sample data initialized successfully");
        } else {
            logger.info("Data already exists, skipping initialization");
        }
    }
    
    private void initializeData() {
        // Create Admin User
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("System Administrator");
        admin.setEmail("admin@medicore.com");
        admin.setPhoneNumber("0123456789");
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);
        
        // Create Doctors
        Doctor doctor1 = new Doctor();
        doctor1.setUsername("dr.nguyen");
        doctor1.setPassword(passwordEncoder.encode("doctor123"));
        doctor1.setFullName("Dr. Nguyen Van A");
        doctor1.setEmail("nguyen@medicore.com");
        doctor1.setPhoneNumber("0987654321");
        doctor1.setSpecialization("Cardiology");
        doctor1.setLicenseNumber("LIC001");
        doctor1.setExperienceYears(10);
        doctorRepository.save(doctor1);
        
        Doctor doctor2 = new Doctor();
        doctor2.setUsername("dr.tran");
        doctor2.setPassword(passwordEncoder.encode("doctor123"));
        doctor2.setFullName("Dr. Tran Thi B");
        doctor2.setEmail("tran@medicore.com");
        doctor2.setPhoneNumber("0976543210");
        doctor2.setSpecialization("Pediatrics");
        doctor2.setLicenseNumber("LIC002");
        doctor2.setExperienceYears(8);
        doctorRepository.save(doctor2);
        
        Doctor doctor3 = new Doctor();
        doctor3.setUsername("dr.le");
        doctor3.setPassword(passwordEncoder.encode("doctor123"));
        doctor3.setFullName("Dr. Le Van C");
        doctor3.setEmail("le@medicore.com");
        doctor3.setPhoneNumber("0965432109");
        doctor3.setSpecialization("Orthopedics");
        doctor3.setLicenseNumber("LIC003");
        doctor3.setExperienceYears(12);
        doctorRepository.save(doctor3);
        
        // Create Patients
        Patient patient1 = new Patient();
        patient1.setUsername("patient1");
        patient1.setPassword(passwordEncoder.encode("patient123"));
        patient1.setFullName("Pham Van D");
        patient1.setEmail("pham@patient.com");
        patient1.setPhoneNumber("0912345678");
        patient1.setDateOfBirth(LocalDate.of(1990, 5, 15));
        patient1.setAddress("123 Nguyen Trai, Ho Chi Minh City");
        patient1.setEmergencyContactName("Pham Thi E");
        patient1.setEmergencyContactPhone("0923456789");
        patient1.setAllergies("Penicillin");
        patient1.setMedicalHistory("Hypertension");
        patientRepository.save(patient1);
        
        Patient patient2 = new Patient();
        patient2.setUsername("patient2");
        patient2.setPassword(passwordEncoder.encode("patient123"));
        patient2.setFullName("Nguyen Thi F");
        patient2.setEmail("nguyen@patient.com");
        patient2.setPhoneNumber("0934567890");
        patient2.setDateOfBirth(LocalDate.of(1985, 10, 20));
        patient2.setAddress("456 Le Loi, Ho Chi Minh City");
        patient2.setEmergencyContactName("Nguyen Van G");
        patient2.setEmergencyContactPhone("0945678901");
        patient2.setAllergies("None");
        patient2.setMedicalHistory("Diabetes Type 2");
        patientRepository.save(patient2);
        
        // Create Medicines
        Medicine medicine1 = new Medicine();
        medicine1.setName("Paracetamol");
        medicine1.setDescription("Pain reliever and fever reducer");
        medicine1.setManufacturer("Pharma Corp");
        medicine1.setPrice(new BigDecimal("15000"));
        medicine1.setStockQuantity(500);
        medicine1.setDosageForm("Tablet");
        medicine1.setStrength("500mg");
        medicine1.setActiveIngredient("Paracetamol");
        medicine1.setSideEffects("Nausea, skin rash (rare)");
        medicine1.setContraindications("Severe liver disease");
        medicineRepository.save(medicine1);
        
        Medicine medicine2 = new Medicine();
        medicine2.setName("Amoxicillin");
        medicine2.setDescription("Antibiotic for bacterial infections");
        medicine2.setManufacturer("BioMed Inc");
        medicine2.setPrice(new BigDecimal("45000"));
        medicine2.setStockQuantity(200);
        medicine2.setDosageForm("Capsule");
        medicine2.setStrength("250mg");
        medicine2.setActiveIngredient("Amoxicillin");
        medicine2.setSideEffects("Diarrhea, nausea, skin rash");
        medicine2.setContraindications("Penicillin allergy");
        medicineRepository.save(medicine2);
        
        Medicine medicine3 = new Medicine();
        medicine3.setName("Ibuprofen");
        medicine3.setDescription("Anti-inflammatory and pain reliever");
        medicine3.setManufacturer("Health Labs");
        medicine3.setPrice(new BigDecimal("25000"));
        medicine3.setStockQuantity(300);
        medicine3.setDosageForm("Tablet");
        medicine3.setStrength("400mg");
        medicine3.setActiveIngredient("Ibuprofen");
        medicine3.setSideEffects("Stomach upset, dizziness");
        medicine3.setContraindications("Peptic ulcer, kidney disease");
        medicineRepository.save(medicine3);
        
        Medicine medicine4 = new Medicine();
        medicine4.setName("Metformin");
        medicine4.setDescription("Diabetes medication");
        medicine4.setManufacturer("DiaCare Pharma");
        medicine4.setPrice(new BigDecimal("35000"));
        medicine4.setStockQuantity(150);
        medicine4.setDosageForm("Tablet");
        medicine4.setStrength("500mg");
        medicine4.setActiveIngredient("Metformin Hydrochloride");
        medicine4.setSideEffects("Nausea, diarrhea, metallic taste");
        medicine4.setContraindications("Kidney dysfunction, liver disease");
        medicineRepository.save(medicine4);
        
        Medicine medicine5 = new Medicine();
        medicine5.setName("Lisinopril");
        medicine5.setDescription("ACE inhibitor for hypertension");
        medicine5.setManufacturer("CardioMed");
        medicine5.setPrice(new BigDecimal("50000"));
        medicine5.setStockQuantity(100);
        medicine5.setDosageForm("Tablet");
        medicine5.setStrength("10mg");
        medicine5.setActiveIngredient("Lisinopril");
        medicine5.setSideEffects("Dry cough, dizziness, fatigue");
        medicine5.setContraindications("Pregnancy, angioedema history");
        medicineRepository.save(medicine5);
        
        logger.info("Initialized data: 1 admin, 3 doctors, 2 patients, 5 medicines");
    }
}