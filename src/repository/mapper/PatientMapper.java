package repository.mapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import enums.BloodType;
import enums.Gender;
import model.Patient;

public class PatientMapper implements BaseMapper<Patient> {
    public  Patient fromCsvString(String csvLine){
        String[] parts = csvLine.split(",");
        
        return new Patient(
            parts[0],                           // id
            parts[1],                           // password
            parts[2].getBytes(StandardCharsets.UTF_8),
            parts[3],                           // name
            Integer.parseInt(parts[4]),         // age
            LocalDate.parse(parts[5]),          // dateOfBirth
            Gender.valueOf(parts[6]),           // gender
            BloodType.valueOf(parts[7]),        // bloodType
            Integer.parseInt(parts[8]),         // phoneNumber
            parts[9],                           // email
            LocalDateTime.parse(parts[10]),      // createdAt
            LocalDateTime.parse(parts[11])      // updatedAt
        );
    }
}