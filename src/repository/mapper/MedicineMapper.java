package repository.mapper;

import java.time.LocalDateTime;

import model.Medicine;

public class MedicineMapper implements BaseMapper<Medicine> {
    public Medicine fromCsvString(String csvLine){
        String[] parts = csvLine.split(",");

        return new Medicine(
            parts[0],
            parts[1],
            Integer.parseInt(parts[2]),
            Integer.parseInt(parts[3]),
            LocalDateTime.parse(parts[4]),
            LocalDateTime.parse(parts[5])
        );
    }
}
