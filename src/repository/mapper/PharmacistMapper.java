package repository.mapper;

import java.time.LocalDateTime;

import enums.Gender;
import model.Pharmacist;

public class PharmacistMapper {
    public static Pharmacist fromCsvString(String csvLine){
        String[] parts = csvLine.split(",");

        return new Pharmacist(
            parts[0],
            parts[1],
            parts[2],
            Integer.parseInt(parts[3]),
            Gender.valueOf(parts[4]),
            LocalDateTime.parse(parts[5]),
            LocalDateTime.parse(parts[6])
        );
    }
}