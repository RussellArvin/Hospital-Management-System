package repository;

import java.util.List;

import model.Prescription;
import repository.base.CsvRepository;
import repository.mapper.PrescriptionMapper;

public class PrescriptionRepository extends CsvRepository<Prescription,PrescriptionMapper> {
    private static final String CSV_FILE = "data/prescriptions.csv";
    private static final String CSV_HEADER = "id,appointmentOutcomeId,medicineId,status,amount,createdAt,updatedAt";

    public PrescriptionRepository() {
        super(CSV_FILE, CSV_HEADER, new PrescriptionMapper());
    }

    @Override
    public Prescription[] findAll() {
        return super.findAll(Prescription.class);
    }

    public Prescription[] findManyByOutcomeId(String outcomeId){
        List<String> lines = this.fileManager.findLinesByColumnValue("appointmentOutcomeId",outcomeId);
        return super.mapLines(lines, Prescription.class);
    }
}