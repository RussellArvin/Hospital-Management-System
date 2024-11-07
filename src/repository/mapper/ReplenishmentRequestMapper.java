package repository.mapper;

import java.time.LocalDateTime;

import enums.ReplenishmentRequestStatus;
import model.ReplenishmentRequest;

public class ReplenishmentRequestMapper implements BaseMapper<ReplenishmentRequest> {
    public ReplenishmentRequest fromCsvString(String csvLine){
        String[] parts = csvLine.split(",");

        return new ReplenishmentRequest(
            parts[0],
            parts[1],
            parts[2],
            Integer.parseInt(parts[3]),
            ReplenishmentRequestStatus.valueOf(parts[4]),
            LocalDateTime.parse(parts[5]),
            LocalDateTime.parse(parts[6])
        );
    }   
}
