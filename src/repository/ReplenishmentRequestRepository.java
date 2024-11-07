package repository;

import model.ReplenishmentRequest;
import repository.base.CsvRepository;
import repository.mapper.ReplenishmentRequestMapper;

public class ReplenishmentRequestRepository extends CsvRepository<ReplenishmentRequest, ReplenishmentRequestMapper> {
    private static final String CSV_FILE = "data/replenishmentrequest.csv";
    private static final String CSV_HEADER = "id,medicineId,pharmacistId,newAmount,status,createdAt,updatedAt";

    public ReplenishmentRequestRepository() {
        super(CSV_FILE, CSV_HEADER, new ReplenishmentRequestMapper());
    }

    @Override
    public ReplenishmentRequest[] findAll() {
        return super.findAll(ReplenishmentRequest.class);
    }
}
