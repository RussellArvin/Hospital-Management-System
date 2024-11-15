package repository;

import java.util.List;
import model.MedicalRecord;
import repository.base.CsvRepository;
import repository.mapper.MedicalRecordMapper;

/**
 * The MedicalRecordRepository class extends CsvRepository for managing MedicalRecord entities.
 * It specifies the CSV file and header used for MedicalRecord data and provides methods
 * for accessing and managing records based on patient IDs.
 * 
 * @author Tan Jou Yuan 
 * @version 1.0
 */
public class MedicalRecordRepository extends CsvRepository<MedicalRecord, MedicalRecordMapper> {

    private static final String CSV_FILE = "data/medical-record.csv";
    private static final String CSV_HEADER = "id,patientId,doctorId,type,details,createdAt,updatedAt";

    /**
     * Constructs a MedicalRecordRepository with predefined CSV file and header information
     * for storing MedicalRecord data and initializes the MedicalRecordMapper.
     */
    public MedicalRecordRepository() {
        super(CSV_FILE, CSV_HEADER, new MedicalRecordMapper());
    }

    /**
     * Finds and returns multiple MedicalRecord records associated with the specified patient ID.
     *
     * @param patientId The patient ID to search for.
     * @return An array of MedicalRecord objects associated with the specified patient ID.
     */
    public MedicalRecord[] findManyByPatientId(String patientId) {
        List<String> lines = this.fileManager.findLinesByColumnValue("patientId", patientId);
        return super.mapLines(lines, MedicalRecord.class);
    }
}
