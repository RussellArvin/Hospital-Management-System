package repository.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import enums.BloodType;
import enums.Gender;
import model.Patient;

public class PatientMapper {
    public static Patient fromCsvString(String csvLine){
        String[] parts = csvLine.split(",");
        
        return new Patient(
            parts[0],                           // id
            parts[1],                           // password
            parts[2],                           // name
            Integer.parseInt(parts[3]),         // age
            LocalDate.parse(parts[4]),          // dateOfBirth
            Gender.valueOf(parts[5]),           // gender
            BloodType.valueOf(parts[6]),        // bloodType
            Integer.parseInt(parts[7]),         // phoneNumber
            parts[8],                           // email
            LocalDateTime.parse(parts[9]),      // createdAt
            LocalDateTime.parse(parts[10])      // updatedAt
        );
    }
}