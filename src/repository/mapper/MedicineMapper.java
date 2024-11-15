package repository.mapper;

import java.time.LocalDateTime;
import model.Medicine;

/**
 * The MedicineMapper class provides a mapping from a CSV line to a Medicine object.
 * It implements the BaseMapper interface for the Medicine type.
 * 
 * @author Lim Jun Howe
 * @version 1.0
 */
public class MedicineMapper implements BaseMapper<Medicine> {

    /**
     * Converts a CSV line into a Medicine object by parsing each field.
     *
     * @param csvLine The CSV line containing Medicine data.
     * @return A Medicine object constructed from the CSV data.
     */
    public Medicine fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");

        return new Medicine(
            parts[0],
            parts[1],
            Integer.parseInt(parts[2]),
            Integer.parseInt(parts[3]),
            LocalDateTime.parse(parts[4]),
            LocalDateTime.parse(parts[5])
        );
    }
}
