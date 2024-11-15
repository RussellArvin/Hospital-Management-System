package repository.mapper;

import enums.PrescriptionStatus;
import java.time.LocalDateTime;
import model.Prescription;

/**
 * The PrescriptionMapper class provides a mapping from a CSV line to a Prescription object.
 * It implements the BaseMapper interface for the Prescription type.
 * 
 * @author Lim Jun Howe 
 * @version 1.0
 */
public class PrescriptionMapper implements BaseMapper<Prescription> {

    /**
     * Converts a CSV line into a Prescription object by parsing each field.
     *
     * @param csvLine The CSV line containing Prescription data.
     * @return A Prescription object constructed from the CSV data.
     */
    public Prescription fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");

        return new Prescription(
            parts[0],                          // id
            parts[1],                          // appointmentOutcomeId
            parts[2],                          // medicineId
            PrescriptionStatus.valueOf(parts[3]), // status
            Integer.parseInt(parts[4]),        // amount
            LocalDateTime.parse(parts[5]),     // createdAt
            LocalDateTime.parse(parts[6])      // updatedAt
        );
    }
}
