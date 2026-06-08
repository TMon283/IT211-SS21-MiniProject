package org.example.miniproj.controller;

import jakarta.validation.Valid;
import org.example.miniproj.dto.request.CreateDoctorRequest;
import org.example.miniproj.dto.request.CreateMedicineRequest;
import org.example.miniproj.dto.request.CreatePatientRequest;
import org.example.miniproj.dto.request.CreateUserRequest;
import org.example.miniproj.dto.response.ApiResponse;
import org.example.miniproj.dto.response.DoctorResponse;
import org.example.miniproj.dto.response.UserInfoResponse;
import org.example.miniproj.entity.User;
import org.example.miniproj.service.DoctorService;
import org.example.miniproj.service.MedicineService;
import org.example.miniproj.service.PatientService;
import org.example.miniproj.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private PatientService patientService;
    
    @Autowired
    private MedicineService medicineService;
    
    // User Management
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserInfoResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            UserInfoResponse response = userService.createUser(request);
            return ResponseEntity.ok(ApiResponse.success("User created successfully", response));
        } catch (Exception e) {
            logger.error("Failed to create user", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserInfoResponse>>> getAllUsers() {
        List<UserInfoResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }
    
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserById(@PathVariable Long id) {
        try {
            UserInfoResponse user = userService.getUserById(id);
            return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserInfoResponse>> updateUser(@PathVariable Long id, @Valid @RequestBody CreateUserRequest request) {
        try {
            UserInfoResponse response = userService.updateUser(id, request);
            return ResponseEntity.ok(ApiResponse.success("User updated successfully", response));
        } catch (Exception e) {
            logger.error("Failed to update user", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
        } catch (Exception e) {
            logger.error("Failed to delete user", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping("/users/{username}/force-logout")
    public ResponseEntity<ApiResponse<String>> forceLogoutUser(@PathVariable String username) {
        try {
            userService.forceLogoutUser(username);
            return ResponseEntity.ok(ApiResponse.success("User force logged out successfully"));
        } catch (Exception e) {
            logger.error("Failed to force logout user", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // Doctor Management
    @PostMapping("/doctors")
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(@Valid @RequestBody CreateDoctorRequest request) {
        try {
            DoctorResponse response = doctorService.createDoctor(request);
            return ResponseEntity.ok(ApiResponse.success("Doctor created successfully", response));
        } catch (Exception e) {
            logger.error("Failed to create doctor", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/doctors")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAllDoctors() {
        List<DoctorResponse> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(ApiResponse.success("Doctors retrieved successfully", doctors));
    }
    
    @GetMapping("/doctors/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctorById(@PathVariable Long id) {
        try {
            DoctorResponse doctor = doctorService.getDoctorById(id);
            return ResponseEntity.ok(ApiResponse.success("Doctor retrieved successfully", doctor));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/doctors/{id}")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(@PathVariable Long id, @Valid @RequestBody CreateDoctorRequest request) {
        try {
            DoctorResponse response = doctorService.updateDoctor(id, request);
            return ResponseEntity.ok(ApiResponse.success("Doctor updated successfully", response));
        } catch (Exception e) {
            logger.error("Failed to update doctor", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<ApiResponse<String>> deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok(ApiResponse.success("Doctor deleted successfully"));
        } catch (Exception e) {
            logger.error("Failed to delete doctor", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    // Medicine Management
    @PostMapping("/medicines")
    public ResponseEntity<ApiResponse<org.example.miniproj.dto.response.MedicineResponse>> createMedicine(@Valid @RequestBody CreateMedicineRequest request) {
        try {
            org.example.miniproj.dto.response.MedicineResponse response = medicineService.createMedicine(request);
            return ResponseEntity.ok(ApiResponse.success("Medicine created successfully", response));
        } catch (Exception e) {
            logger.error("Failed to create medicine", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/medicines")
    public ResponseEntity<ApiResponse<List<org.example.miniproj.dto.response.MedicineResponse>>> getAllMedicines() {
        List<org.example.miniproj.dto.response.MedicineResponse> medicines = medicineService.getAllMedicines();
        return ResponseEntity.ok(ApiResponse.success("Medicines retrieved successfully", medicines));
    }
    
    @GetMapping("/medicines/{id}")
    public ResponseEntity<ApiResponse<org.example.miniproj.dto.response.MedicineResponse>> getMedicineById(@PathVariable Long id) {
        try {
            org.example.miniproj.dto.response.MedicineResponse medicine = medicineService.getMedicineById(id);
            return ResponseEntity.ok(ApiResponse.success("Medicine retrieved successfully", medicine));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/medicines/{id}")
    public ResponseEntity<ApiResponse<org.example.miniproj.dto.response.MedicineResponse>> updateMedicine(@PathVariable Long id, @Valid @RequestBody CreateMedicineRequest request) {
        try {
            org.example.miniproj.dto.response.MedicineResponse response = medicineService.updateMedicine(id, request);
            return ResponseEntity.ok(ApiResponse.success("Medicine updated successfully", response));
        } catch (Exception e) {
            logger.error("Failed to update medicine", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/medicines/{id}")
    public ResponseEntity<ApiResponse<String>> deleteMedicine(@PathVariable Long id) {
        try {
            medicineService.deleteMedicine(id);
            return ResponseEntity.ok(ApiResponse.success("Medicine deleted successfully"));
        } catch (Exception e) {
            logger.error("Failed to delete medicine", e);
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}