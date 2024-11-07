package repository.mapper;

import java.time.LocalDateTime;

import enums.AppointmentStatus;
import model.Appointment;

public class AppointmentMapper implements BaseMapper<Appointment> {
    public Appointment fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");

        return new Appointment (
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
