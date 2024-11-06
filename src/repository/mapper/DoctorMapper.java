package repository.mapper;

import java.time.LocalDateTime;

import enums.Gender;
import model.Doctor;

public class DoctorMapper {
    public static Doctor fromCsvString(String csvLine){
        String[] parts = csvLine.split(",");

        return new Doctor(
            parts[0],
            parts[1],
            parts[2],
            Integer.parseInt(parts[3]),
            Gender.valueOf(parts[4]),
            Integer.parseInt(parts[5]),
            Integer.parseInt(parts[6]),
            LocalDateTime.parse(parts[7]),
            LocalDateTime.parse(parts[8])
        );
    }
}