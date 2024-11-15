package repository.mapper;

import enums.Gender;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import model.Administrator;

/**
 * The AdministratorMapper class provides a mapping from a CSV line to an Administrator object.
 * It implements the BaseMapper interface for the Administrator type.
 * 
 * @author Natalyn Pong 
 * @version 1.0
 */
public class AdministratorMapper implements BaseMapper<Administrator> {

    /**
     * Converts a CSV line into an Administrator object by parsing each field.
     *
     * @param csvLine The CSV line containing Administrator data.
     * @return An Administrator object constructed from the CSV data.
     */
    public Administrator fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");

        return new Administrator(
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
