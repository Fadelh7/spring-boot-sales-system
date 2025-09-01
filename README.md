# Spring Boot Sales System - Complete Implementation

## � Quick Start Guide

### Prerequisites
- **JDK 17 or 21** (JDK 24 not compatible)
- **Windows PowerShell** (for running commands)

### Step 1: Setup Environment
```powershell
# Set JAVA_HOME to JDK 17 or 21
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"  # Adjust path to your JDK
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

# Verify Java version
java -version  # Should show 17.x or 21.x
```

### Step 2: Build and Start the Application
```powershell
# Navigate to project directory
cd "C:\Users\Lenovo\Documents\GitHub\spring-boot-quiz"

# Build and test the project
.\mvnw.cmd clean verify

# Start the application
.\mvnw.cmd spring-boot:run
```

**✅ Application will start on http://localhost:8080**

### Step 3: Test with Swagger UI (Interactive API Documentation)

1. **Open Swagger UI**: http://localhost:8080/swagger-ui.html
2. **Create a Product**:
   - Click on `product-controller` → `POST /products`
   - Click "Try it out"
   - Use this sample data:
   ```json
   {
     "name": "Laptop",
     "description": "Gaming laptop",
     "category": "Electronics"
   }
   ```
   - Click "Execute" → Should return `201 Created`

3. **Create a Client**:
   - Go to `client-controller` → `POST /clients`
   - Use sample data:
   ```json
   {
     "firstName": "John",
     "lastName": "Doe", 
     "mobile": "+1234567890"
   }
   ```

4. **Create a Sale**:
   - Go to `sale-controller` → `POST /sales`
   - Use sample data:
   ```json
   {
     "clientId": 1,
     "seller": "Alice Smith",
     "transactions": [
       {
         "productId": 1,
         "quantity": 2,
         "price": 999.99
       }
     ]
   }
   ```

5. **View Results**: Use GET endpoints to see created data and check audit logs in console

### Step 4: Test with Postman Collection

1. **Import Collection**:
   - Open Postman
   - Click "Import" → "Upload Files"
   - Select: `postman/Sales-System-API.postman_collection.json`

2. **Set Base URL**:
   - Edit collection → Variables tab
   - Set `baseUrl` = `http://localhost:8080`

3. **Run Test Sequence**:
   - **Create Product** → Execute request
   - **Create Client** → Execute request  
   - **Create Sale** → Execute request
   - **Get All Sales** → Verify data
   - **Update Sale** → Test quantity/price changes

### Step 5: Database Console (Optional)
- **Access H2 Console**: http://localhost:8080/h2-console
- **Connection Settings**:
  - JDBC URL: `jdbc:h2:mem:salesdb`
  - Username: `sa`
  - Password: (leave blank)
- **Query Data**: `SELECT * FROM SALE;` to see sales with audit logs

---

## �📋 Project Overview

This is a comprehensive Spring Boot REST API implementation for a simple sales system that manages Products, Clients, and Sales operations with complete audit logging. The project fulfills all requirements of the Spring Boot Quiz specification.

## ✅ Requirements Compliance Check

### **Products Management** ✅ FULLY IMPLEMENTED
- ✅ **Fetch products**: `GET /products` - Returns id, name, description, category, creation date
- ✅ **Create new product**: `POST /products` - Creates product with validation
- ✅ **Update existing product**: `PUT /products/{id}` - Updates product with validation

### **Clients Management** ✅ FULLY IMPLEMENTED  
- ✅ **Fetch clients**: `GET /clients` - Returns id, firstName, lastName, mobile
- ✅ **Create new client**: `POST /clients` - Creates client with validation
- ✅ **Edit existing client**: `PUT /clients/{id}` - Updates client with validation

### **Sales Management** ✅ FULLY IMPLEMENTED
- ✅ **Fetch all sales**: `GET /sales` - Returns id, creationDate, client, seller, total
- ✅ **Create new sales with multiple transactions**: `POST /sales` - Supports multiple products per sale
- ✅ **Edit quantities and prices**: `PUT /sales/{id}` - Updates sale transactions with recalculated totals

### **Logging** ✅ FULLY IMPLEMENTED
- ✅ **Log all update operations on sale transactions** - Event-driven audit logging with `@TransactionalEventListener`

### **Required Files** ✅ FULLY DELIVERED
- ✅ **Class diagram file**: `docs/uml/class-diagram.puml` - PlantUML format showing entity relationships
- ✅ **Github project**: Complete project with Maven build, tests, CI/CD, documentation

---

## 🚀 What We Built - Step by Step Implementation

### **1. Project Setup & Configuration**
**What we did**: Created Spring Boot 3 Maven project structure
**Why**: Establish foundation with modern Spring Boot and Java 17/21 support
**Output**: 
- `pom.xml` with dependencies (Web, JPA, Validation, H2, Swagger, Lombok, Tests)
- `application.yml` with H2 database configuration
- Maven Wrapper (`mvnw.cmd`) for consistent builds
- `.gitignore` and `LICENSE` files

### **2. Domain Model Design**
**What we did**: Designed JPA entities with proper relationships
**Why**: Create normalized database schema with referential integrity
**Output**: 
- `Product.java` - id, name, description, category, creationDate (auto-set)
- `Client.java` - id, firstName, lastName, mobile  
- `Sale.java` - id, creationDate, client (FK), seller, total (calculated)
- `SaleTransaction.java` - id, product (FK), quantity, price, sale (FK)
- **Relationships**: Client 1→N Sale, Sale 1→N SaleTransaction, Product 1→N SaleTransaction

### **3. Data Access Layer**
**What we did**: Created Spring Data JPA repositories
**Why**: Provide CRUD operations with minimal boilerplate
**Output**: 
- `ProductRepository`, `ClientRepository`, `SaleRepository`, `SaleTransactionRepository`
- All extending `JpaRepository<Entity, Long>` for standard database operations

### **4. Data Transfer Objects (DTOs)**
**What we did**: Created DTOs with validation annotations for API contracts
**Why**: Decouple internal domain from external API, enable validation, prevent over-exposure
**Output**:
- `ProductDto`, `ClientDto` with `@NotBlank`, `@Size` validations
- `SaleTransactionDto` with `@NotNull`, `@Min` validations  
- `SaleDtos` (nested classes): `SaleCreateRequest`, `SaleUpdateRequest`, `SaleResponse`, `SaleTransactionResponse`

### **5. Mapping Layer**
**What we did**: Created utility class for entity ↔ DTO conversions
**Why**: Centralize mapping logic, calculate derived fields (line totals)
**Output**: 
- `Mappers.java` with static methods for all conversions
- Line total calculation: `price × quantity` for transaction responses

### **6. Business Logic Layer**
**What we did**: Implemented service interfaces and business logic
**Why**: Encapsulate business rules, transaction management, validation
**Output**:
- `ProductService`/`ProductServiceImpl` - CRUD with existence checks
- `ClientService`/`ClientServiceImpl` - CRUD with existence checks  
- `SaleService`/`SaleServiceImpl` - Complex create/update with:
  - Client/Product existence validation
  - Multiple transaction processing
  - Total calculation and recalculation
  - Event publishing for audit logging

### **7. REST API Controllers**
**What we did**: Created REST endpoints with proper HTTP semantics
**Why**: Expose business functionality via HTTP API with OpenAPI documentation
**Output**:
- `ProductController` - GET, POST, PUT `/products`
- `ClientController` - GET, POST, PUT `/clients`  
- `SaleController` - GET, POST, PUT `/sales`
- All with `@Valid` validation, proper HTTP status codes, Swagger tags

### **8. Audit Logging Implementation**
**What we did**: Event-driven logging for sale transaction updates
**Why**: Meet requirement for logging all sale transaction updates asynchronously
**Output**:
- `SaleTransactionUpdateLogger` with `@TransactionalEventListener(AFTER_COMMIT)`
- `SaleUpdatedEvent` record for type-safe event data
- Published from `SaleServiceImpl` on create/update operations
- Logs: `[AUDIT] Sale {id} updated by seller='{name}' with {count} transactions`

### **9. Exception Handling**
**What we did**: Global exception handling with proper HTTP responses
**Why**: Provide consistent error responses, handle validation failures
**Output**:
- `GlobalExceptionHandler` with `@ControllerAdvice`
- `NotFoundException` for 404 responses
- Validation error handling with field-level details
- Structured error responses with timestamp, status, message

### **10. API Documentation**
**What we did**: Integrated Swagger/OpenAPI 3 documentation
**Why**: Provide interactive API documentation and testing interface
**Output**:
- `OpenApiConfig` with API metadata
- Swagger UI available at `/swagger-ui.html`
- Schema annotations on DTOs for better documentation

### **11. Database Configuration**
**What we did**: H2 in-memory database for development
**Why**: Enable easy development/testing without external database setup
**Output**:
- H2 console at `/h2-console` (JDBC: `jdbc:h2:mem:salesdb`, user: `sa`)
- Hibernate auto-DDL to create tables from entities
- SQL logging enabled for debugging

### **12. Testing Strategy**
**What we did**: Unit and integration tests with proper test structure
**Why**: Ensure code quality, verify business logic, test API integration
**Output**:
- `ProductServiceImplTest` - Unit test with Mockito
- `SalesFlowIT` - Integration test with full Spring context
- `SalesSystemApplicationTests` - Context loading smoke test
- Test coverage for create/update scenarios with assertions

### **13. Build & CI/CD**
**What we did**: Maven build with GitHub Actions CI pipeline
**Why**: Automate builds, run tests, ensure consistent deployment
**Output**:
- `.github/workflows/ci.yml` - Build and test on push/PR
- Maven configuration with Java 17+ requirement
- Maven Enforcer plugin for JDK version compliance

### **14. Documentation & Class Diagram**
**What we did**: PlantUML class diagram and comprehensive documentation
**Why**: Visualize entity relationships, provide project overview
**Output**:
- `docs/uml/class-diagram.puml` - Entity relationship diagram
- Shows Client→Sale→SaleTransaction→Product relationships
- This README with complete implementation details

### **15. API Testing Collection**
**What we did**: Postman collection with comprehensive test scenarios
**Why**: Enable easy API testing and demonstrate functionality
**Output**:
- `postman/Sales-System-API.postman_collection.json`
- Sample data setup, CRUD operations, error testing
- Variables for easy testing workflow

---

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.3.3
- **Language**: Java 17+ (Compatible with 17, 21)
- **Database**: H2 (in-memory for development)
- **ORM**: Spring Data JPA / Hibernate
- **Documentation**: Swagger/OpenAPI 3  
- **Testing**: JUnit 5, Mockito, Spring Boot Test
- **Build**: Maven 3.9.7 (with wrapper)
- **CI/CD**: GitHub Actions

## 🚀 Quick Start

### Prerequisites
- JDK 17 or 21 (required - JDK 24 not compatible)
- No Maven installation needed (uses wrapper)

### Running the Application

1. **Set JAVA_HOME to JDK 17/21**:
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
java -version  # Should show 17.x or 21.x
```

2. **Build and test**:
```powershell
.\mvnw.cmd -q -DskipTests=false clean verify
```

3. **Run the application**:
```powershell
.\mvnw.cmd spring-boot:run
```

### Access Points
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:salesdb`
  - Username: `sa`
  - Password: (leave blank)

## 📋 API Endpoints

### Products Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/products` | List all products (id, name, description, category, creationDate) |
| POST | `/products` | Create new product |
| PUT | `/products/{id}` | Update existing product |

### Clients Management  
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/clients` | List all clients (id, firstName, lastName, mobile) |
| POST | `/clients` | Create new client |
| PUT | `/clients/{id}` | Update existing client |

### Sales Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/sales` | List all sales (id, creationDate, client, seller, total) |
| POST | `/sales` | Create new sale with multiple transactions |
| PUT | `/sales/{id}` | Update sale quantities and prices |

## 📊 Audit Logging

All sale transaction updates are automatically logged with the format:
```
[AUDIT] Sale {saleId} updated by seller='{sellerName}' with {transactionCount} transactions
```

This logging occurs asynchronously after transaction commit using Spring's `@TransactionalEventListener`.

## 🧪 Testing

- **Import Postman Collection**: `postman/Sales-System-API.postman_collection.json`
- **Run Unit Tests**: `.\mvnw.cmd test`
- **Run Integration Tests**: `.\mvnw.cmd verify`

## 📁 Project Structure

```
src/
├── main/java/com/example/salessystem/
│   ├── SalesSystemApplication.java          # Main application entry point
│   ├── domain/                              # JPA entities
│   │   ├── Product.java                     # Product entity with creationDate
│   │   ├── Client.java                      # Client entity  
│   │   ├── Sale.java                        # Sale entity with total calculation
│   │   └── SaleTransaction.java             # Transaction entity
│   ├── repository/                          # Data access layer
│   ├── dto/                                 # Data transfer objects with validation
│   ├── service/                             # Business logic interfaces
│   ├── service/impl/                        # Business logic implementations
│   ├── controller/                          # REST API endpoints
│   ├── mapper/                              # Entity ↔ DTO conversions
│   ├── exception/                           # Exception handling
│   ├── aspect/                              # Audit logging
│   └── config/                              # Configuration classes
├── main/resources/
│   └── application.yml                      # Application configuration
└── test/java/                               # Unit and integration tests
docs/uml/class-diagram.puml                 # PlantUML class diagram
postman/Sales-System-API.postman_collection.json  # API testing collection
```



