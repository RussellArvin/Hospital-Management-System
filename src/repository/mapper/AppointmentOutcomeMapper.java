package repository.mapper;

import java.time.LocalDateTime;

import enums.AppointmentServiceType;
import model.AppointmentOutcome;

public class AppointmentOutcomeMapper implements BaseMapper<AppointmentOutcome> {
    public  AppointmentOutcome fromCsvString(String csvLine){
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
