package repository;

import java.util.List;
import model.Prescription;
import repository.base.CsvRepository;
import repository.mapper.PrescriptionMapper;

/**
 * The PrescriptionRepository class extends CsvRepository for managing Prescription entities.
 * It specifies the CSV file and header used for Prescription data and provides methods
 * for accessing and managing Prescription records based on appointment outcome IDs.
 * 
 * @author Russell Arvin 
 * @version 1.0
 */
public class PrescriptionRepository extends CsvRepository<Prescription, PrescriptionMapper> {

    private static final String CSV_FILE = "data/prescriptions.csv";
    private static final String CSV_HEADER = "id,appointmentOutcomeId,medicineId,status,amount,createdAt,updatedAt";

    /**
     * Constructs a PrescriptionRepository with predefined CSV file and header information
     * for storing Prescription data and initializes the PrescriptionMapper.
     */
    public PrescriptionRepository() {
        super(CSV_FILE, CSV_HEADER, new PrescriptionMapper());
    }

    /**
     * Retrieves all Prescription records from the CSV file.
     *
     * @return An array of all Prescription objects in the repository.
     */
    @Override
    public Prescription[] findAll() {
        return super.findAll(Prescription.class);
    }

    /**
     * Finds and returns multiple Prescription records associated with the specified appointment outcome ID.
     *
     * @param outcomeId The appointment outcome ID to search for.
     * @return An array of Prescription objects associated with the specified outcome ID.
     */
    public Prescription[] findManyByOutcomeId(String outcomeId) {
        List<String> lines = this.fileManager.findLinesByColumnValue("appointmentOutcomeId", outcomeId);
        return super.mapLines(lines, Prescription.class);
    }
}
