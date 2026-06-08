# MediCore API - Deployment Guide

## 🎯 Tóm tắt Dự án

**MediCore API** là hệ thống Backend hoàn chỉnh cho nền tảng y tế số, được phát triển theo yêu cầu của khách hàng Eco-Health. Dự án bao gồm:

### ✅ Các Tính năng Đã Triển khai

#### 🔐 Hệ thống Bảo mật JWT Đa tầng
- **Access Token**: 30 phút (cho API calls)
- **Refresh Token**: 30 ngày (để làm mới Access Token)
- **Token Blacklist**: Thu hồi token khi logout hoặc admin force logout
- **Phân quyền**: ADMIN, DOCTOR, PATIENT với Spring Security @PreAuthorize

#### 👥 Quản lý Người dùng
- **User Management**: CRUD operations cho tất cả users
- **Doctor Management**: Quản lý thông tin bác sĩ, chuyên khoa, license
- **Patient Management**: Quản lý hồ sơ bệnh nhân, thông tin y tế

#### 🏥 Nghiệp vụ Y tế Core
- **Appointment System**: Đặt lịch, xem lịch, cập nhật trạng thái
- **Prescription Management**: Kê đơn thuốc chi tiết với liều lượng
- **Medicine Catalog**: Quản lý danh mục thuốc với đầy đủ thông tin
- **Medical Records**: Lưu trữ và truy xuất hồ sơ bệnh án

#### 🛡️ Bảo mật & Logging
- **Exception Handling**: Global exception handler với AOP
- **Audit Logging**: Ghi log chi tiết các hoạt động hệ thống
- **Input Validation**: Validation đầu vào với Bean Validation
- **Security Configuration**: CORS, CSRF protection

#### 📊 Testing & Quality Assurance
- **Unit Tests**: Service và Controller tests với Mockito
- **Integration Tests**: Test API endpoints
- **Jacoco Coverage**: Báo cáo độ phủ code
- **Postman Collection**: Test cases đầy đủ cho tất cả APIs

## 📁 Cấu trúc Code Đã Tạo

```
src/main/java/org/example/miniproj/
├── MiniprojApplication.java              # Main application với @EnableScheduling
├── config/
│   ├── SecurityConfig.java               # JWT Security configuration
│   └── DataInitializer.java              # Sample data initialization
├── controller/
│   ├── AuthController.java               # Authentication endpoints
│   ├── AdminController.java              # Admin management APIs
│   ├── DoctorController.java             # Doctor operations APIs
│   └── PatientController.java            # Patient operations APIs
├── dto/
│   ├── request/                          # Request DTOs với validation
│   │   ├── LoginRequest.java
│   │   ├── RefreshTokenRequest.java
│   │   ├── CreateUserRequest.java
│   │   ├── CreateDoctorRequest.java
│   │   ├── CreatePatientRequest.java
│   │   ├── CreateMedicineRequest.java
│   │   ├── CreateAppointmentRequest.java
│   │   └── CreatePrescriptionRequest.java
│   └── response/                         # Response DTOs
│       ├── ApiResponse.java
│       ├── JwtAuthenticationResponse.java
│       ├── UserInfoResponse.java
│       ├── DoctorResponse.java
│       ├── PatientResponse.java
│       ├── MedicineResponse.java
│       ├── AppointmentResponse.java
│       └── PrescriptionResponse.java
├── entity/                               # JPA Entities với Lombok
│   ├── User.java                         # Base user entity
│   ├── Doctor.java                       # Doctor entity (extends User)
│   ├── Patient.java                      # Patient entity (extends User)
│   ├── Medicine.java                     # Medicine catalog
│   ├── Appointment.java                  # Appointment booking
│   ├── Prescription.java                 # Prescription management
│   ├── PrescriptionItem.java             # Prescription details
│   └── TokenBlacklist.java               # JWT token blacklist
├── exception/
│   ├── GlobalExceptionHandler.java       # AOP exception handling
│   ├── TokenExpiredException.java
│   ├── ResourceNotFoundException.java
│   └── DuplicateResourceException.java
├── repository/                           # Spring Data JPA với Stream API
│   ├── UserRepository.java
│   ├── DoctorRepository.java
│   ├── PatientRepository.java
│   ├── MedicineRepository.java
│   ├── AppointmentRepository.java
│   ├── PrescriptionRepository.java
│   └── TokenBlacklistRepository.java
├── security/                             # JWT Security components
│   ├── JwtTokenProvider.java             # JWT token generation/validation
│   ├── JwtAuthenticationFilter.java      # JWT filter chain
│   └── JwtAuthenticationEntryPoint.java  # Auth entry point
└── service/                              # Business Logic với Stream API
    ├── CustomUserDetailsService.java
    ├── TokenBlacklistService.java
    ├── AuthService.java                  # Login/logout/refresh
    ├── UserService.java                  # User CRUD operations
    ├── DoctorService.java                # Doctor management
    ├── PatientService.java               # Patient management
    ├── MedicineService.java              # Medicine catalog
    ├── AppointmentService.java           # Appointment booking
    └── PrescriptionService.java          # Prescription management
```

## 📋 File Configuration

### build.gradle
- Spring Boot 4.0.6 với Java 21
- Dependencies: JPA, Security, Validation, JWT, Lombok, MySQL
- Jacoco plugin cho test coverage

### application.properties
- MySQL database configuration (có thể switch sang H2 cho development)
- JWT token expiration settings
- Logging configuration
- Jackson JSON formatting

## 🧪 Testing Suite

### Unit Tests (src/test/)
- `AuthServiceTest.java`: Test login, refresh token, logout flows
- `UserServiceTest.java`: Test user CRUD operations
- `AuthControllerTest.java`: Test authentication endpoints

### Postman Collection
- `MediCore_API_Collection.json`: Đầy đủ test cases cho tất cả APIs
- Bao gồm authentication flow, role-based access, security tests

## 📖 Documentation

### README.md
- Hướng dẫn setup và chạy dự án
- Giải thích các API endpoints
- Luồng authentication JWT
- Dữ liệu mẫu và test accounts

### DEPLOYMENT_GUIDE.md (file này)
- Tổng quan kỹ thuật
- Cấu trúc code
- Hướng dẫn deployment

## 🚀 Environment Setup

### Yêu cầu
- Java 21+
- MySQL 8.0+ (hoặc H2 cho development)
- Gradle 8.11+

### Cách chạy
```bash
# 1. Clone và setup database
git clone <repository>
cd miniproj

# 2. Cấu hình MySQL trong application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/medicore_db
spring.datasource.username=root
spring.datasource.password=your_password

# 3. Build và chạy
./gradlew clean build
./gradlew bootRun

# 4. Import Postman collection và test APIs
```

## 🔑 Test Accounts

| Role | Username | Password | Email |
|------|----------|----------|--------|
| ADMIN | admin | admin123 | admin@medicore.com |
| DOCTOR | dr.nguyen | doctor123 | nguyen@medicore.com |
| DOCTOR | dr.tran | doctor123 | tran@medicore.com |
| PATIENT | patient1 | patient123 | pham@patient.com |
| PATIENT | patient2 | patient123 | nguyen@patient.com |

## 📊 API Testing Workflow

### 1. Authentication Flow
```
1. POST /api/auth/login → Get Access & Refresh tokens
2. Use Access Token trong Authorization header
3. POST /api/auth/refresh khi Access Token expires
4. POST /api/auth/logout để blacklist tokens
```

### 2. Security Test Cases
- ✅ Role-based access control
- ✅ Token expiration handling  
- ✅ Token blacklist validation
- ✅ Input validation
- ✅ Cross-role access prevention

### 3. Business Logic Tests
- ✅ User management (CRUD)
- ✅ Doctor-Patient appointment booking
- ✅ Prescription creation và viewing
- ✅ Medicine catalog management

## 🎯 Deliverables Hoàn thành

### ✅ Source Code
- Hoàn chỉnh monolithic architecture với Spring Boot
- Tuân thủ RESTful API standards
- Sử dụng Stream API thay vì traditional arrays
- Code quality cao với Lombok và proper validation

### ✅ Postman Collection
- Đầy đủ test scenarios theo yêu cầu
- Authentication flow testing
- Security validation tests
- Business logic verification

### ✅ Documentation
- README.md chi tiết với setup guide
- API documentation với examples
- Security flow explanation

### ✅ Testing
- Unit tests cho core services
- Controller integration tests
- Jacoco test coverage reports

## 🔧 Production Considerations

### Database Migration
- Chuyển từ H2 sang MySQL/PostgreSQL
- Database indexing cho performance
- Connection pooling configuration

### Security Enhancements
- HTTPS enforcement
- Rate limiting
- API versioning
- CORS policy refinement

### Monitoring & Logging
- Application monitoring (actuator endpoints)
- Centralized logging (ELK stack)
- Performance metrics
- Health check endpoints

### Scalability
- Redis cho token caching
- Database read replicas
- Load balancing considerations
- Microservices migration path

---

**✅ Dự án MediCore API đã hoàn thành đầy đủ theo yêu cầu SRS, sẵn sàng cho demo và production deployment.**