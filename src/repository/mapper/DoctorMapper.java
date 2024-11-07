package repository.mapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import enums.Gender;
import model.Doctor;

public class DoctorMapper implements BaseMapper<Doctor> {
    public Doctor fromCsvString(String csvLine){
        String[] parts = csvLine.split(",");

        return new Doctor(
            parts[0],
            parts[1],
            parts[2].getBytes(StandardCharsets.UTF_8),
            parts[3],
            Integer.parseInt(parts[4]),
            Gender.valueOf(parts[5]),
            Integer.parseInt(parts[6]),
            Integer.parseInt(parts[7]),
            LocalDateTime.parse(parts[8]),
            LocalDateTime.parse(parts[9])
        );
    }
}