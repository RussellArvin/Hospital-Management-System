package repository;

import java.util.List;

import enums.ReplenishmentRequestStatus;
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

    public ReplenishmentRequest[] findManyByStatus(ReplenishmentRequestStatus status){
        List<String> lines = this.fileManager.findLinesByColumnValue("status",status.toString());
        return super.mapLines(lines, ReplenishmentRequest.class);
    }

    public ReplenishmentRequest[] findManyByPharmacist(String pharmarcistId){
        List<String> lines = this.fileManager.findLinesByColumnValue("pharmacistId",pharmarcistId);
        return super.mapLines(lines, ReplenishmentRequest.class);
    }
}
