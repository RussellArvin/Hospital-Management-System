package repository.mapper;

import enums.Gender;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import model.Nurse;

/**
 * The NurseMapper class provides a mapping from a CSV line to a Nurse object.
 * It implements the BaseMapper interface for the Nurse type.
 * 
 * @author Tan Jou Yuan 
 * @version 1.0
 */
public class NurseMapper implements BaseMapper<Nurse> {

    /**
     * Converts a CSV line into a Nurse object by parsing each field.
     *
     * @param csvLine The CSV line containing Nurse data.
     * @return A Nurse object constructed from the CSV data.
     */
    public Nurse fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");

        return new Nurse(
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
