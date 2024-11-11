package repository.mapper;

import java.time.LocalDateTime;

import model.PatientVital;

public class PatientVitalMapper implements BaseMapper<PatientVital> {
    public   PatientVital fromCsvString(String csvLine){
        String[] parts = csvLine.split(",");
        return new PatientVital(
            parts[0],
            parts[1],
            Integer.parseInt(parts[2]),
            Integer.parseInt(parts[3]),
            Integer.parseInt(parts[4]),
            Integer.parseInt(parts[5]),
            LocalDateTime.parse(parts[6]),
            LocalDateTime.parse(parts[7])
        );
    }
}
