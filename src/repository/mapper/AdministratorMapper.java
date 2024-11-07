package repository.mapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import model.Administrator;

public class AdministratorMapper {
    public static Administrator fromCsvString(String csvLine){
        String[] parts = csvLine.split(",");

        return new Administrator(
            parts[0],
            parts[1],
             parts[2].getBytes(StandardCharsets.UTF_8),
            parts[3],
            LocalDateTime.parse(parts[4]),
            LocalDateTime.parse(parts[5])
        );
    }
}