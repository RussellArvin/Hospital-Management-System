# Hospital Management System (HMS)

A comprehensive Java-based command-line interface application for managing hospital operations, featuring a custom CSV-based persistence layer and automated data initialization from Excel files.

## 📑 Table of Contents

1. [Project Overview](#-project-overview)
2. [Design Considerations](#-design-considerations)
   - [Basic Object-Oriented Principles](#basic-object-oriented-principles)
   - [SOLID Principles](#solid-principles)
   - [Design Choices and Trade-offs](#design-choices-and-trade-offs)
3. [Getting Started](#-getting-started)
   - [Prerequisites](#prerequisites)
   - [Installation](#installation)
4. [System Initialization](#-system-initialization)
   - [Initial Data Structure](#initial-data-structure)
   - [Excel File Formats](#excel-file-formats)
   - [Default Credentials](#default-credentials)
5. [Architecture](#-architecture)
   - [Project Structure](#project-structure)
   - [Custom CSV Persistence Layer](#custom-csv-persistence-layer)
6. [Key Features](#-key-features)
   - [Multi-Role User System](#multi-role-user-system)
   - [Additional Features](#additional-features)
7. [Reflection](#-reflection)
   - [Technical Challenges](#technical-challenges)
   - [Lessons Learned](#lessons-learned)
   - [Future Improvements](#future-improvements)
8. [License](#-license)
9. [Contact](#-contact)
10. [Acknowledgments](#-acknowledgments)

## 🏥 Project Overview

The Hospital Management System (HMS) automates hospital operations through a robust object-oriented architecture, facilitating efficient management of hospital resources, patient care, and administrative processes.

## 🎯 Design Considerations

### Basic Object-Oriented Principles

- **Encapsulation**: Private fields with public getters/setters in all models
- **Inheritance**: Base classes for common functionality (`BaseEntity`, `BaseRepository`)
- **Polymorphism**: Common interfaces for different user types
- **Abstraction**: Abstract base classes and interfaces defining contracts

### SOLID Principles

#### Single Responsibility Principle (SRP)
- Each class has one primary responsibility
- Example: `CsvFileManager` handles only file operations, while `DoctorMapper` handles only Doctor entity mapping

#### Open/Closed Principle (OCP)
- Base classes are open for extension but closed for modification
- Example: `CsvRepository<T>` can be extended for new entity types without modifying the base implementation

#### Liskov Substitution Principle (LSP)
- Derived classes can be substituted for their base classes
- Example: Any `UserEntity` can be used where base user functionality is needed

#### Interface Segregation Principle (ISP)
- Specific interfaces for different roles (Doctor, Nurse, Pharmacist)
- Clients only need to implement relevant methods

#### Dependency Injection Principle (DIP)
- High-level modules depend on abstractions
- Services receive repositories through constructor injection

### Design Choices and Trade-offs

1. **CSV-based Storage**
   - Pros: Simple, portable, human-readable
   - Cons: Limited query capabilities, no transactions
   - Trade-off: Simplicity over performance

2. **Generic Repository Pattern**
   - Pros: Reusable code, type safety
   - Cons: Some boilerplate code
   - Trade-off: Code reuse over minimal complexity

3. **Service Layer Architecture**
   - Pros: Clear separation of concerns
   - Cons: Additional layer of abstraction
   - Trade-off: Maintainability over direct access

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Microsoft Excel (for initial data files)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/RussellArvin/hospital-management-system.git
```

2. Navigate to project directory:
```bash
cd hospital-management-system
```

3. Run the compilation script:

**For Windows:**
```bash
compile.bat
```

**For Unix/Linux/MacOS:**
```bash
./compile.sh
```

## 📊 System Initialization

The system features an automated initialization process that seeds the database with initial data from Excel files when the system is first run.

### Initial Data Structure

```
initial-data/
├── Staff_List.xlsx      # Staff members data
├── Patient_List.xlsx    # Patient records
└── Medicine_List.xlsx   # Medicine inventory
```

### Excel File Formats

#### Staff_List.xlsx
```
ID | Name      | Role         | Gender | Age
D1 | Dr. Smith | DOCTOR       | M      | 45
N1 | Jane Doe  | NURSE        | F      | 32
P1 | John Park | PHARMACIST   | M      | 28
A1 | Admin One | ADMINISTRATOR| F      | 35
```

#### Patient_List.xlsx
```
ID | Name    | DOB        | Gender | BloodType | Email
P1 | J. Doe  | 1990-01-01 | M      | A+        | jdoe@email.com
```

#### Medicine_List.xlsx
```
Name          | Stock | LowStockAlert
Paracetamol   | 1000  | 100
```

### Default Credentials

```
All Users:
- Default Password: "password"

Administrator: 
- ID format: A[number] (e.g., A1)

Doctor:
- ID format: D[number] (e.g., D1)

Nurse:
- ID format: N[number] (e.g., N1)

Pharmacist:
- ID format: P[number] (e.g., P1)

Patient:
- ID format: P[number] (e.g., P1)
```

## 🏗️ Architecture

### Project Structure

```
src/
├── ui/              # User interface components
├── controller/      # Request handlers
├── service/         # Business logic
├── repository/      # Data access layer
│   ├── base/        # Base repository components
│   ├── mapper/      # Data mappers
├── model/           # Domain entities
├── util/            # Utility classes
└── enums/          # Enumerations

data/               # CSV data storage
├── doctors.csv
├── nurses.csv
├── patients.csv
├── pharmacists.csv
├── medical.csv
├── appointments.csv
├── prescriptions.csv
├── vitals.csv
└── medicines.csv
```

### Custom CSV Persistence Layer

#### Base Repository
```java
public abstract class CsvRepository<T extends BaseEntity, M extends BaseMapper<T>> {
    protected final CsvFileManager fileManager;
    protected final M mapper;

    // CRUD operations
    public void save(T entity)
    public T findOne(String id)
    public T[] findAll()
    public void update(T entity)
    public void delete(T entity)
}
```

## 🔑 Key Features

### Multi-Role User System
- **Patient Portal**
  - View and manage medical records
  - Schedule appointments
  - Update personal information
  - View appointment history
  
- **Doctor Interface**
  - Manage patient records
  - Handle appointments
  - Issue prescriptions
  - Set availability
  
- **Pharmacist Dashboard**
  - Manage prescriptions
  - Track medicine inventory
  - Handle stock alerts
  
- **Admin Console**
  - Manage staff accounts
  - System configuration
  - Access control
  - Monitor system operations

### Additional Features

1. **Enhanced Nurse Module**
   - Vital signs tracking and monitoring
   - Viewing of appointments for the day
   - Viewing patient records

2. **Advanced Table Navigation**
   - Column sorting capabilities
   - Pagination for large datasets
   - Dynamic table rendering

3. **Architectural Enhancements**
   - Generic repository implementation
   - Custom ORM-like CSV persistence
   - Robust error handling system
   - Data validation framework
   - Service layer abstraction

## 💭 Reflection

### Technical Challenges
- Implementing thread-safe CSV operations
- Designing a flexible repository pattern
- Managing data relationships without a proper database
- Handling concurrent user access

### Lessons Learned
- Importance of proper architecture planning
- Value of SOLID principles in maintainable code
- Benefits of generic programming
- Challenges of file-based storage systems
- Impact of good error handling

### Future Improvements
1. Migration to a proper database
2. Implementation of caching layer
3. Enhanced concurrent access handling
4. More sophisticated search capabilities
5. User interface improvements

## 📝 License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## 📞 Contact

Russell Arvin  
Project Link: [https://github.com/RussellArvin/hospital-management-system](https://github.com/RussellArvin/hospital-management-system)

## 🙏 Acknowledgments

- CZ2002 Object-Oriented Design & Programming course
- Course instructors and teaching assistants
- Open source community