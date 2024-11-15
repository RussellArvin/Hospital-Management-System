package repository.mapper;

import enums.Gender;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import model.Pharmacist;

/**
 * The PharmacistMapper class provides a mapping from a CSV line to a Pharmacist object.
 * It implements the BaseMapper interface for the Pharmacist type.
 * 
 * @author Natalyn Pong 
 * @version 1.0
 */
public class PharmacistMapper implements BaseMapper<Pharmacist> {

    /**
     * Converts a CSV line into a Pharmacist object by parsing each field.
     *
     * @param csvLine The CSV line containing Pharmacist data.
     * @return A Pharmacist object constructed from the CSV data.
     */
    public Pharmacist fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");

        return new Pharmacist(
            parts[0],                          // id
            parts[1],                          // password
            parts[2].getBytes(StandardCharsets.UTF_8), // salt
            parts[3],                          // name
            Integer.parseInt(parts[4]),        // age
            Gender.valueOf(parts[5]),          // gender
            LocalDateTime.parse(parts[6]),     // createdAt
            LocalDateTime.parse(parts[7])      // updatedAt
        );
    }
}
