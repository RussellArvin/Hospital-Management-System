package repository.mapper;

import enums.AppointmentStatus;
import java.time.LocalDateTime;
import model.Appointment;

/**
 * The AppointmentMapper class provides a mapping from a CSV line to an Appointment object.
 * It implements the BaseMapper interface for the Appointment type.
 * 
 * @author Lim Jun Howe
 * @version 1.0 
 */
public class AppointmentMapper implements BaseMapper<Appointment> {

    /**
     * Converts a CSV line into an Appointment object by parsing each field.
     *
     * @param csvLine The CSV line containing Appointment data.
     * @return An Appointment object constructed from the CSV data.
     */
    public Appointment fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");

        return new Appointment(
            parts[0],
            parts[1],
            parts[2],
            LocalDateTime.parse(parts[3]),
            LocalDateTime.parse(parts[4]),
            AppointmentStatus.valueOf(parts[5]),
            parts[6],
            LocalDateTime.parse(parts[7]),
            LocalDateTime.parse(parts[8])
        );
    }
}
