package repository.mapper;

import java.time.LocalDateTime;

import model.Administrator;

public class AdministratorMapper {
    public static Administrator fromCsvString(String csvLine){
        String[] parts = csvLine.split(",");

        return new Administrator(
            parts[0],
            parts[1],
            parts[2],
            LocalDateTime.parse(parts[3]),
            LocalDateTime.parse(parts[4])
        );
    }
}