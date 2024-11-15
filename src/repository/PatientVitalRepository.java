package repository;

import java.util.List;
import model.PatientVital;
import repository.base.CsvRepository;
import repository.mapper.PatientVitalMapper;

/**
 * The PatientVitalRepository class extends CsvRepository for managing PatientVital entities.
 * It specifies the CSV file and header used for PatientVital data and provides methods
 * for accessing and managing PatientVital records based on patient IDs.
 * 
 * @author Lim Jun Howe 
 * @version 1.0
 */
public class PatientVitalRepository extends CsvRepository<PatientVital, PatientVitalMapper> {

    private static final String CSV_FILE = "data/patient-vitals.csv";
    private static final String CSV_HEADER = "id,patientId,bloodOxygen,height,weight,bloodPressure,createdAt,updatedAt";

    /**
     * Constructs a PatientVitalRepository with predefined CSV file and header information
     * for storing PatientVital data and initializes the PatientVitalMapper.
     */
    public PatientVitalRepository() {
        super(CSV_FILE, CSV_HEADER, new PatientVitalMapper());
    }

    /**
     * Finds and returns multiple PatientVital records associated with the specified patient ID.
     *
     * @param patientId The patient ID to search for.
     * @return An array of PatientVital objects associated with the specified patient ID.
     */
    public PatientVital[] findManyByPatientId(String patientId) {
        List<String> lines = this.fileManager.findLinesByColumnValue("patientId", patientId);
        return super.mapLines(lines, PatientVital.class);
    }
}
