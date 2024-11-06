package repository.mapper;

import java.time.LocalDate;

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
            LocalDate.parse(parts[3]),          // dateOfBirth
            Gender.valueOf(parts[4]),           // gender
            BloodType.valueOf(parts[5]),        // bloodType
            Integer.parseInt(parts[6]),         // phoneNumber
            parts[7]                            // email
        );
    }
}
