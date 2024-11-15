package repository;

import model.Patient;
import repository.base.CsvRepository;
import repository.mapper.PatientMapper;

/**
 * The PatientRepository class extends CsvRepository for managing Patient entities.
 * It specifies the CSV file and header used for Patient data and provides methods
 * for accessing and managing Patient records based on their name.
 * 
 * @author Natalyn Pong 
 * @version 1.0
 */
public class PatientRepository extends CsvRepository<Patient, PatientMapper> {

    private static final String CSV_FILE = "data/patients.csv";
    private static final String CSV_HEADER = "id,password,salt,name,age,dateOfBirth,gender,bloodType,phoneNumber,email,createdAt,updatedAt";

    /**
     * Constructs a PatientRepository with predefined CSV file and header information
     * for storing Patient data and initializes the PatientMapper.
     */
    public PatientRepository() {
        super(CSV_FILE, CSV_HEADER, new PatientMapper());
    }

    /**
     * Retrieves all Patient records from the CSV file.
     *
     * @return An array of all Patient objects in the repository.
     */
    @Override
    public Patient[] findAll() {
        return super.findAll(Patient.class);
    }

    /**
     * Finds and returns a Patient record by the specified name.
     *
     * @param patientName The name of the patient to search for.
     * @return The Patient object with the specified name, or null if not found.
     */
    public Patient findOneByName(String patientName) {
        String line = this.fileManager.findLineByColumnValue("name", patientName);
        return this.mapper.fromCsvString(line);
    }
}
