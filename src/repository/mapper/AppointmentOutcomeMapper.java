package repository.mapper;

import java.time.LocalDateTime;

import enums.AppointmentServiceType;
import model.AppointmentOutcome;

public class AppointmentOutcomeMapper {
    public static AppointmentOutcome fromCsvString(String csvLine){
        String[] parts = csvLine.split(",");
        return new AppointmentOutcome(
            parts[0],
            parts[1],
            AppointmentServiceType.valueOf(parts[2]),
            parts[3],
            LocalDateTime.parse(parts[4]),
            LocalDateTime.parse(parts[5])
        );
    }
}
