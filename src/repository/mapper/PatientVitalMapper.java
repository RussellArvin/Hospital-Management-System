package repository.mapper;

import java.time.LocalDateTime;
import model.PatientVital;

/**
 * The PatientVitalMapper class provides a mapping from a CSV line to a PatientVital object.
 * It implements the BaseMapper interface for the PatientVital type.
 * 
 * @author Celeste Ho 
 * @verion 1.0
 */
public class PatientVitalMapper implements BaseMapper<PatientVital> {

    /**
     * Converts a CSV line into a PatientVital object by parsing each field.
     *
     * @param csvLine The CSV line containing PatientVital data.
     * @return A PatientVital object constructed from the CSV data.
     */
    public PatientVital fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");
        return new PatientVital(
            parts[0],                          // id
            parts[1],                          // patientId
            Integer.parseInt(parts[2]),        // bloodOxygen
            Integer.parseInt(parts[3]),        // height
            Integer.parseInt(parts[4]),        // weight
            Integer.parseInt(parts[5]),        // bloodPressure
            LocalDateTime.parse(parts[6]),     // createdAt
            LocalDateTime.parse(parts[7])      // updatedAt
        );
    }
}
