package repository.mapper;

import enums.MedicalRecordType;
import java.time.LocalDateTime;
import model.MedicalRecord;

/**
 * The MedicalRecordMapper class provides a mapping from a CSV line to a MedicalRecord object.
 * It implements the BaseMapper interface for the MedicalRecord type.
 * 
 * @author Natalyn Pong 
 * @version 1.0 
 */
public class MedicalRecordMapper implements BaseMapper<MedicalRecord> {

    /**
     * Converts a CSV line into a MedicalRecord object by parsing each field.
     *
     * @param csvLine The CSV line containing MedicalRecord data.
     * @return A MedicalRecord object constructed from the CSV data.
     */
    public MedicalRecord fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");

        return new MedicalRecord(
            parts[0],
            parts[1],
            parts[2],
            MedicalRecordType.valueOf(parts[3]),
            parts[4],
            LocalDateTime.parse(parts[5]),
            LocalDateTime.parse(parts[6])
        );
    }
}
