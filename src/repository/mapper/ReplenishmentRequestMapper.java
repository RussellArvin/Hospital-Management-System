package repository.mapper;

import enums.ReplenishmentRequestStatus;
import java.time.LocalDateTime;
import model.ReplenishmentRequest;

/**
 * The ReplenishmentRequestMapper class provides a mapping from a CSV line to a ReplenishmentRequest object.
 * It implements the BaseMapper interface for the ReplenishmentRequest type.
 * 
 * @author Tan Jou Yuan 
 * @version 1.0
 */
public class ReplenishmentRequestMapper implements BaseMapper<ReplenishmentRequest> {

    /**
     * Converts a CSV line into a ReplenishmentRequest object by parsing each field.
     *
     * @param csvLine The CSV line containing ReplenishmentRequest data.
     * @return A ReplenishmentRequest object constructed from the CSV data.
     */
    public ReplenishmentRequest fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");
        return new ReplenishmentRequest(
            parts[0],                          // id
            parts[1],                          // medicineId
            parts[2],                          // pharmacistId
            Integer.parseInt(parts[3]),        // newAmount
            ReplenishmentRequestStatus.valueOf(parts[4]), // status
            LocalDateTime.parse(parts[5]),     // createdAt
            LocalDateTime.parse(parts[6])      // updatedAt
        );
    }
}
