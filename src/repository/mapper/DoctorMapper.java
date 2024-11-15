package repository.mapper;

import enums.Gender;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import model.Doctor;

/**
 * The DoctorMapper class provides a mapping from a CSV line to a Doctor object.
 * It implements the BaseMapper interface for the Doctor type.
 * 
 * @author Celeste Ho 
 * @version 1.0
 */
public class DoctorMapper implements BaseMapper<Doctor> {

    /**
     * Converts a CSV line into a Doctor object by parsing each field.
     *
     * @param csvLine The CSV line containing Doctor data.
     * @return A Doctor object constructed from the CSV data.
     */
    public Doctor fromCsvString(String csvLine) {
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
