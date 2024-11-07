package repository.mapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import enums.Gender;
import model.Pharmacist;

public class PharmacistMapper implements BaseMapper<Pharmacist> {
    public Pharmacist fromCsvString(String csvLine){
        String[] parts = csvLine.split(",");

        return new Pharmacist(
            parts[0],
            parts[1],
            parts[2].getBytes(StandardCharsets.UTF_8),
            parts[3],
            Integer.parseInt(parts[4]),
            Gender.valueOf(parts[5]),
            LocalDateTime.parse(parts[6]),
            LocalDateTime.parse(parts[7])
        );
    }
}