package repository;

import java.io.*;
import java.util.*;
import java.time.LocalDate;
import model.Patient;
import model.User;
import enums.Gender;
import enums.BloodType;

public class PatientRepository {
    private static final String CSV_FILE = "data/patients.csv";
    private static final String CSV_HEADER = "id,password,name,dateOfBirth,gender,bloodType,phone,email";

    // Constructor - creates data directory and CSV file if they don't exist
    public PatientRepository() {
        createCsvIfNotExists();
    }

    public Patient findOne(String id) {
    try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
        String line;
        boolean firstLine = true;
        
        while ((line = reader.readLine()) != null) {
            // Skip header line
            if (firstLine) {
                firstLine = false;
                continue;
            }
            
            // Parse the line into patient
            Patient patient = fromCsvString(line);
            
            // Check if this is the patient we're looking for
            if (patient.getId().equals(id)) {
                return patient;
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading CSV while finding patient: " + e.getMessage());
    }
    
    // Return null if patient not found
    return null;
}

// You might also want to modify your fromCsvString method to handle date better:
private Patient fromCsvString(String csvLine) {
    String[] parts = csvLine.split(",");
    if (parts.length < 8) {
        throw new IllegalArgumentException("Invalid CSV format");
    }

    try {
        return new Patient(
            parts[0],                           // id
            parts[1],                           // password
            parts[2],                           // name
            LocalDate.parse(parts[3]),          // dateOfBirth - parse string to LocalDate
            Gender.valueOf(parts[4]),           // gender
            BloodType.valueOf(parts[5]),        // bloodType
            Integer.parseInt(parts[6]),         // phone - parse string to int
            parts[7]                            // email
        );
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Error parsing CSV data: " + e.getMessage());
    }
}

    // Main function to save patient to CSV
    // public void save(Patient patient) {
    //     try {
    //         // Check if patient already exists
    //         List<Patient> patients = loadAllPatients();
    //         boolean exists = false;
            
    //         // Create or append to file
    //         try (FileWriter fw = new FileWriter(CSV_FILE, true);
    //              BufferedWriter bw = new BufferedWriter(fw);
    //              PrintWriter out = new PrintWriter(bw)) {
                
    //             // If file is empty, write header
    //             if (new File(CSV_FILE).length() == 0) {
    //                 out.println(CSV_HEADER);
    //             }
                
    //             // Write patient data
    //             out.println(patient.toCsvString());
                
    //         } catch (IOException e) {
    //             System.err.println("Error writing to CSV: " + e.getMessage());
    //             throw e;
    //         }
            
    //     } catch (IOException e) {
    //         System.err.println("Failed to save patient: " + e.getMessage());
    //     }
    // }

    // Helper method to create CSV file and directory if they don't exist
    private void createCsvIfNotExists() {
        File file = new File(CSV_FILE);
        file.getParentFile().mkdirs();  // Create data directory if it doesn't exist
        
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println(CSV_HEADER);
            } catch (IOException e) {
                System.err.println("Error creating CSV file: " + e.getMessage());
            }
        }
    }

//     // Helper method to load all patients (useful for checking duplicates)
//     private List<Patient> loadAllPatients() {
//         List<Patient> patients = new ArrayList<>();
//         try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
//             String line;
//             boolean firstLine = true;
//             while ((line = reader.readLine()) != null) {
//                 if (firstLine) {  // Skip header
//                     firstLine = false;
//                     continue;
//                 }
//                 Patient patient = new Patient();
//                 patient.fromCsvString(line);
//                 patients.add(patient);
//             }
//         } catch (IOException e) {
//             System.err.println("Error reading CSV: " + e.getMessage());
//         }
//         return patients;
//     }
 }