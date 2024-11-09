package repository;

import model.Prescription;
import repository.base.CsvRepository;
import repository.mapper.PrescriptionMapper;

public class PrescriptionRepository extends CsvRepository<Prescription,PrescriptionMapper> {
    private static final String CSV_FILE = "data/prescriptions.csv";
    private static final String CSV_HEADER = "id,appointmentOutcomeId,medicineId,status,createdAt,updatedAt";

    public PrescriptionRepository() {
        super(CSV_FILE, CSV_HEADER, new PrescriptionMapper());
    }

    @Override
    public Prescription[] findAll() {
        return super.findAll(Prescription.class);
    }
}
