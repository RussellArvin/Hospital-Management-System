package repository.mapper;

import enums.BloodType;
import enums.Gender;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import model.Patient;

/**
 * The PatientMapper class provides a mapping from a CSV line to a Patient object.
 * It implements the BaseMapper interface for the Patient type.
 * 
 * @author Russell Arvin 
 * @version 1.0
 */
public class PatientMapper implements BaseMapper<Patient> {

    /**
     * Converts a CSV line into a Patient object by parsing each field.
     *
     * @param csvLine The CSV line containing Patient data.
     * @return A Patient object constructed from the CSV data.
     */
    public Patient fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");
        
        return new Patient(
            parts[0],                           // id
            parts[1],                           // password
            parts[2].getBytes(StandardCharsets.UTF_8), // salt
            parts[3],                           // name
            Integer.parseInt(parts[4]),         // age
            LocalDate.parse(parts[5]),          // dateOfBirth
            Gender.valueOf(parts[6]),           // gender
            BloodType.valueOf(parts[7]),        // bloodType
            Integer.parseInt(parts[8]),         // phoneNumber
            parts[9],                           // email
            LocalDateTime.parse(parts[10]),     // createdAt
            LocalDateTime.parse(parts[11])      // updatedAt
        );
    }
}
