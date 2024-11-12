package service;

import model.*;
import model.Nurse;
import repository.*;
import util.*;
import enums.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Service responsible for initializing the system with data from Excel files.
 * This class handles the creation and persistence of initial data for:
 * <ul>
 *   <li>Patients</li>
 *   <li>Staff (Doctors, Nurses, Pharmacists, Administrators)</li>
 *   <li>Medicines</li>
 * </ul>
 * 
 * The service reads data from Excel files and creates corresponding objects
 * with default passwords and necessary validation.
 *
 * @author [Your Name]
 * @version 1.0
 */
public class InitialisationService {
    private static final String MEDICINE_FILE = "initial-data/Medicine_List.xlsx";
    private static final String PATIENT_FILE = "initial-data/Patient_List.xlsx";
    private static final String STAFF_FILE = "initial-data/Staff_List.xlsx";

    private final AdministratorRepository administratorRepository;
    private final PatientRepository patientRepository;
    private final PharmacistRepository pharmacistRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;
    private final MedicineRepository medicineRepository;

    /**
     * Constructs a new InitialisationService with required repositories.
     *
     * @param administratorRepository Repository for Administrator entities
     * @param patientRepository Repository for Patient entities
     * @param pharmacistRepository Repository for Pharmacist entities
     * @param doctorRepository Repository for Doctor entities
     * @param nurseRepository Repository for Nurse entities
     * @param medicineRepository Repository for Medicine entities
     */
    public InitialisationService(
            AdministratorRepository administratorRepository,
            PatientRepository patientRepository,
            PharmacistRepository pharmacistRepository,
            DoctorRepository doctorRepository,
            NurseRepository nurseRepository,
            MedicineRepository medicineRepository) {
        this.administratorRepository = administratorRepository;
        this.patientRepository = patientRepository;
        this.pharmacistRepository = pharmacistRepository;
        this.doctorRepository = doctorRepository;
        this.nurseRepository = nurseRepository;
        this.medicineRepository = medicineRepository;
    }

    /**
     * Initializes the system by creating all necessary entities if they don't exist.
     * This method checks if administrators exist before initializing to prevent
     * duplicate initialization.
     *
     * @return null if initialization is successful, error message if an exception occurs
     */
    public String initialise() {
        try {
            Administrator[] admins = administratorRepository.findAll();
            if (admins.length != 0) return null;

            System.out.println("Initialising System...");
            createPatient();
            createStaff();
            createMedicine();
            return null;
        } catch (Exception e) {
            return "The following occurred when trying to initialise system: " + e.toString();
        }
    }

    /**
     * Creates staff members from Excel file data.
     * Reads staff information and creates appropriate objects based on role.
     * Staff members are created with default passwords and current timestamp.
     *
     * @throws IllegalArgumentException if data format is invalid
     * @throws RuntimeException if there's an error reading the file
     */
    private void createStaff() {
        try {
            List<String> lines = ExcelReader.readExcel(STAFF_FILE);
            List<Doctor> doctors = new ArrayList<>();
            List<Nurse> nurses = new ArrayList<>();
            List<Pharmacist> pharmacists = new ArrayList<>();
            List<Administrator> administrators = new ArrayList<>();
            
            LocalDateTime currentDateTime = LocalDateTime.now();
            
            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                validateDataFormat(parts, 5, i);
                
                String id = parts[0].trim();
                String name = parts[1].trim();
                UserRole role = parseRole(parts[2].trim());
                Gender gender = Gender.valueOf(parts[3].trim().toUpperCase());
                int age = Integer.parseInt(parts[4].trim());
                
                if (role == UserRole.PATIENT) continue;
                
                byte[] salt = PasswordUtil.generateSalt();
                String hashedPassword = PasswordUtil.hashPassword(Constant.DEFAULT_PASSWORD, salt);
                
                createStaffMember(
                    id, hashedPassword, salt, name, age, gender, role,
                    currentDateTime, doctors, nurses, pharmacists, administrators
                );
            }
            
            saveStaffToRepositories(doctors, nurses, pharmacists, administrators);
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid age value in Excel file: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error creating staff from Excel file: " + e.getMessage());
        }
    }

    /**
     * Creates a staff member of the appropriate type and adds it to the corresponding list.
     *
     * @param id Staff ID
     * @param hashedPassword Hashed password
     * @param salt Password salt
     * @param name Staff name
     * @param age Staff age
     * @param gender Staff gender
     * @param role Staff role
     * @param currentDateTime Current timestamp
     * @param doctors List of doctors
     * @param nurses List of nurses
     * @param pharmacists List of pharmacists
     * @param administrators List of administrators
     */
    private void createStaffMember(
            String id, String hashedPassword, byte[] salt, String name, 
            int age, Gender gender, UserRole role, LocalDateTime currentDateTime,
            List<Doctor> doctors, List<Nurse> nurses, 
            List<Pharmacist> pharmacists, List<Administrator> administrators) {
        
        switch (role) {
            case DOCTOR:
                validateStaffId(id, "D");
                doctors.add(new Doctor(id, hashedPassword, salt, name, age, gender,
                    Constant.DEFAULT_START_WORK_HOURS, Constant.DEFAULT_END_WORK_HOURS,
                    currentDateTime, currentDateTime));
                break;
                case NURSE:
                validateStaffId(id, "N");
                nurses.add(new Nurse(
                    id,                  // Staff ID
                    hashedPassword,      // Hashed password
                    salt,               // Salt
                    name,               // Name
                    age,                // Age
                    gender,             // Gender
                    currentDateTime,    // Created at
                    currentDateTime     // Updated at
                ));
                break;
            case PHARMACIST:
                validateStaffId(id, "P");
                pharmacists.add(new Pharmacist(id, hashedPassword, salt, name, age,
                    gender, currentDateTime, currentDateTime));
                break;
            case ADMINISTRATOR:
                validateStaffId(id, "A");
                administrators.add(new Administrator(id, hashedPassword, salt, name,
                    age, gender, currentDateTime, currentDateTime));
                break;
        }
    }

    /**
     * Saves all staff members to their respective repositories.
     */
    private void saveStaffToRepositories(
            List<Doctor> doctors, List<Nurse> nurses,
            List<Pharmacist> pharmacists, List<Administrator> administrators) {
        doctorRepository.saveMany(doctors.toArray(new Doctor[0]));
        nurseRepository.saveMany(nurses.toArray(new Nurse[0]));
        pharmacistRepository.saveMany(pharmacists.toArray(new Pharmacist[0]));
        administratorRepository.saveMany(administrators.toArray(new Administrator[0]));
    }

    /**
     * Creates patient records from Excel file data.
     *
     * @throws DateTimeParseException if date format is invalid
     * @throws IllegalArgumentException if data format is invalid
     * @throws RuntimeException if there's an error reading the file
     */
    private void createPatient() {
        try {
            List<String> lines = ExcelReader.readExcel(PATIENT_FILE);
            Patient[] patients = new Patient[lines.size()];
            
            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                validateDataFormat(parts, 6, i);
                
                byte[] salt = PasswordUtil.generateSalt();
                String hashedPassword = PasswordUtil.hashPassword(Constant.DEFAULT_PASSWORD, salt);
                
                LocalDate dob = LocalDate.parse(parts[2].trim());
                int age = Period.between(dob, LocalDate.now()).getYears();
                Gender gender = Gender.valueOf(parts[3].trim().toUpperCase());
                BloodType bloodType = parseBloodType(parts[4].trim());
                String email = parts[5].trim();
                
                patients[i] = new Patient(
                    parts[0].trim(), hashedPassword, salt, parts[1].trim(),
                    age, dob, gender, bloodType, 0, email
                );
            }
            
            patientRepository.saveMany(patients);
            
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format in Excel file: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error creating patients from Excel file: " + e.getMessage());
        }
    }

    /**
     * Creates medicine records from Excel file data.
     *
     * @throws IllegalArgumentException if data format is invalid
     * @throws RuntimeException if there's an error reading the file
     */
    private void createMedicine() {
        try {
            List<String> lines = ExcelReader.readExcel(MEDICINE_FILE);
            Medicine[] medicines = new Medicine[lines.size()];
            
            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                validateDataFormat(parts, 3, i);
                
                String name = parts[0].trim();
                int stock = parseAndValidateInteger(parts[1].trim(), "stock", i);
                int lowStockAlert = parseAndValidateInteger(parts[2].trim(), "low stock alert", i);
                
                validateMedicineData(name, stock, lowStockAlert, i);
                
                medicines[i] = new Medicine(name, stock, lowStockAlert);
            }
            
            medicineRepository.saveMany(medicines);
            
        } catch (Exception e) {
            throw new RuntimeException("Error creating medicines from Excel file: " + e.getMessage());
        }
    }

    /**
     * Validates the format of data read from Excel.
     *
     * @param parts Array of data parts
     * @param expectedLength Expected number of fields
     * @param lineNumber Line number in Excel file (for error messages)
     * @throws IllegalArgumentException if validation fails
     */
    private void validateDataFormat(String[] parts, int expectedLength, int lineNumber) {
        if (parts.length != expectedLength) {
            throw new IllegalArgumentException(
                "Invalid data format at line " + (lineNumber + 1) +
                ". Expected " + expectedLength + " values but got " + parts.length
            );
        }
    }

    /**
     * Parses blood type string to BloodType enum.
     *
     * @param bloodTypeStr Blood type string from Excel
     * @return Corresponding BloodType enum value
     */
    private BloodType parseBloodType(String bloodTypeStr) {
        return BloodType.valueOf(
            bloodTypeStr.replace("+", "_POSITIVE").replace("-", "_NEGATIVE")
        );
    }

    /**
     * Parses role string to UserRole enum.
     *
     * @param roleStr Role string from Excel
     * @return Corresponding UserRole enum value
     * @throws IllegalArgumentException if role is invalid or is PATIENT
     */
    private UserRole parseRole(String roleStr) {
        try {
            UserRole role = UserRole.valueOf(roleStr.toUpperCase());
            if (role == UserRole.PATIENT) {
                throw new IllegalArgumentException("Patient role is not valid for staff creation");
            }
            return role;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid staff role: " + roleStr);
        }
    }

    /**
     * Validates staff ID format.
     *
     * @param id Staff ID
     * @param expectedPrefix Expected prefix for the role (D, N, P, or A)
     * @throws IllegalArgumentException if ID format is invalid
     */
    private void validateStaffId(String id, String expectedPrefix) {
        if (!id.startsWith(expectedPrefix)) {
            throw new IllegalArgumentException(
                "Invalid ID format. Expected prefix '" + expectedPrefix + "' but got: " + id
            );
        }
    }

    /**
     * Parses and validates integer values from Excel data.
     *
     * @param value String value to parse
     * @param fieldName Field name for error messages
     * @param lineNumber Line number in Excel file
     * @return Parsed integer value
     * @throws IllegalArgumentException if value is invalid
     */
    private int parseAndValidateInteger(String value, String fieldName, int lineNumber) {
        try {
            int parsedValue = Integer.parseInt(value);
            if (parsedValue < 0) {
                throw new IllegalArgumentException(
                    fieldName + " cannot be negative at line " + (lineNumber + 1)
                );
            }
            return parsedValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid " + fieldName + " value at line " + (lineNumber + 1) + ": " + value
            );
        }
    }

    /**
     * Validates medicine data according to business rules.
     *
     * @param name Medicine name
     * @param stock Initial stock
     * @param lowStockAlert Low stock alert level
     * @param lineNumber Line number in Excel file
     * @throws IllegalArgumentException if validation fails
     */
    private void validateMedicineData(String name, int stock, int lowStockAlert, int lineNumber) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Medicine name cannot be empty at line " + (lineNumber + 1)
            );
        }
        
        if (lowStockAlert > stock) {
            throw new IllegalArgumentException(
                "Low stock alert level cannot be higher than initial stock at line " + (lineNumber + 1)
            );
        }
    }
}