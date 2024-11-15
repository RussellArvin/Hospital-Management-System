package repository;

import enums.ReplenishmentRequestStatus;
import java.util.List;
import model.ReplenishmentRequest;
import repository.base.CsvRepository;
import repository.mapper.ReplenishmentRequestMapper;

/**
 * The ReplenishmentRequestRepository class extends CsvRepository for managing ReplenishmentRequest entities.
 * It specifies the CSV file and header used for ReplenishmentRequest data and provides methods
 * for accessing and managing ReplenishmentRequest records based on their status or pharmacist ID.
 * 
 * @author Celeste Ho 
 * @version 1.0
 */
public class ReplenishmentRequestRepository extends CsvRepository<ReplenishmentRequest, ReplenishmentRequestMapper> {

    private static final String CSV_FILE = "data/replenishmentrequest.csv";
    private static final String CSV_HEADER = "id,medicineId,pharmacistId,newAmount,status,createdAt,updatedAt";

    /**
     * Constructs a ReplenishmentRequestRepository with predefined CSV file and header information
     * for storing ReplenishmentRequest data and initializes the ReplenishmentRequestMapper.
     */
    public ReplenishmentRequestRepository() {
        super(CSV_FILE, CSV_HEADER, new ReplenishmentRequestMapper());
    }

    /**
     * Finds and returns multiple ReplenishmentRequest records with the specified status.
     *
     * @param status The status of the replenishment requests to search for.
     * @return An array of ReplenishmentRequest objects with the specified status.
     */
    public ReplenishmentRequest[] findManyByStatus(ReplenishmentRequestStatus status) {
        List<String> lines = this.fileManager.findLinesByColumnValue("status", status.toString());
        return super.mapLines(lines, ReplenishmentRequest.class);
    }

    /**
     * Finds and returns multiple ReplenishmentRequest records associated with the specified pharmacist ID.
     *
     * @param pharmarcistId The pharmacist ID to search for.
     * @return An array of ReplenishmentRequest objects associated with the specified pharmacist ID.
     */
    public ReplenishmentRequest[] findManyByPharmacist(String pharmarcistId) {
        List<String> lines = this.fileManager.findLinesByColumnValue("pharmacistId", pharmarcistId);
        return super.mapLines(lines, ReplenishmentRequest.class);
    }
}
