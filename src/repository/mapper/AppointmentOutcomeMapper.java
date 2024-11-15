package repository.mapper;

import enums.AppointmentServiceType;
import java.time.LocalDateTime;
import model.AppointmentOutcome;

/**
 * The AppointmentOutcomeMapper class provides a mapping from a CSV line to an AppointmentOutcome object.
 * It implements the BaseMapper interface for the AppointmentOutcome type.
 * 
 * @author Tan Jou Yuan 
 * @version 1.0 
 */
public class AppointmentOutcomeMapper implements BaseMapper<AppointmentOutcome> {

    /**
     * Converts a CSV line into an AppointmentOutcome object by parsing each field.
     *
     * @param csvLine The CSV line containing AppointmentOutcome data.
     * @return An AppointmentOutcome object constructed from the CSV data.
     */
    public AppointmentOutcome fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");
        return new AppointmentOutcome(
            parts[0],
            parts[1],
            parts[2],
            AppointmentServiceType.valueOf(parts[3]),
            parts[4],
            LocalDateTime.parse(parts[5]),
            LocalDateTime.parse(parts[6])
        );
    }
}
