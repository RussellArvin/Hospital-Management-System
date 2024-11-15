package repository;

import java.util.List;
import model.AppointmentOutcome;
import repository.base.CsvRepository;
import repository.mapper.AppointmentOutcomeMapper;

/**
 * The AppointmentOutcomeRepository class extends CsvRepository for managing AppointmentOutcome entities.
 * It specifies the CSV file and header used for AppointmentOutcome data and provides methods
 * for accessing and managing records based on appointment and patient IDs.
 * 
 * @author Celeste Ho 
 * @version 1.0
 */
public class AppointmentOutcomeRepository extends CsvRepository<AppointmentOutcome, AppointmentOutcomeMapper> {

    private static final String CSV_FILE = "data/appointment-outcomes.csv";
    private static final String CSV_HEADER = "id,patientId,appointmentId,serviceType,consultationNotes,createdAt,updatedAt";

    /**
     * Constructs an AppointmentOutcomeRepository with predefined CSV file and header information
     * for storing AppointmentOutcome data and initializes the AppointmentOutcomeMapper.
     */
    public AppointmentOutcomeRepository() {
        super(CSV_FILE, CSV_HEADER, new AppointmentOutcomeMapper());
    }

    /**
     * Finds and returns a single AppointmentOutcome by the specified appointment ID.
     *
     * @param appointmentId The appointment ID to search for.
     * @return The AppointmentOutcome object associated with the specified appointment ID, or null if not found.
     */
    public AppointmentOutcome findOneByAppointmentId(String appointmentId) {
        String line = this.fileManager.findLineByColumnValue("appointmentId", appointmentId);
        
        if(line == null) return null;
        return mapper.fromCsvString(line);
    }


    /**
     * Finds and returns multiple AppointmentOutcome records associated with the specified patient ID.
     *
     * @param patientId The patient ID to search for.
     * @return An array of AppointmentOutcome objects associated with the specified patient ID.
     */
    public AppointmentOutcome[] findManyByPatientId(String patientId) {
        List<String> lines = this.fileManager.findLinesByColumnValue("patientId", patientId);
        return super.mapLines(lines, AppointmentOutcome.class);
    }
}
