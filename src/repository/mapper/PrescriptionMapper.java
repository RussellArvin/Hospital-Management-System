package repository.mapper;

import java.time.LocalDateTime;

import enums.PrescriptionStatus;
import model.Prescription;

public class PrescriptionMapper implements BaseMapper<Prescription> {
    public Prescription fromCsvString(String csvLine){
        String[] parts = csvLine.split(",");

        return new Prescription(
            parts[0],
            parts[1],
            parts[2],
            PrescriptionStatus.valueOf(parts[3]),
            Integer.parseInt(parts[4]),
            LocalDateTime.parse(parts[5]),
            LocalDateTime.parse(parts[6])
        );
    }
}
