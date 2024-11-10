package repository.mapper;

import java.time.LocalDateTime;

import enums.MedicalRecordType;
import model.MedicalRecord;

public class MedicalRecordMapper implements BaseMapper<MedicalRecord> {
    public MedicalRecord fromCsvString(String csvLine){
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
