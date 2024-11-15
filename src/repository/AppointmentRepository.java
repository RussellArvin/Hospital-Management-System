package repository;

import enums.AppointmentStatus;
import java.util.List;
import model.Appointment;
import repository.base.CsvRepository;
import repository.mapper.AppointmentMapper;

/**
 * The AppointmentRepository class extends CsvRepository for managing Appointment entities.
 * It specifies the CSV file and header used for Appointment data and provides methods
 * for accessing and managing appointments based on doctor ID, patient ID, and status.
 * 
 * @author Natalyn Pong 
 * @version 1.0
 */
public class AppointmentRepository extends CsvRepository<Appointment, AppointmentMapper> {

    private static final String CSV_FILE = "data/appointments.csv";
    private static final String CSV_HEADER = "id,doctorId,patientId,startDateTime,endDateTime,status,cancelReason,createdAt,updatedAt";

    /**
     * Constructs an AppointmentRepository with predefined CSV file and header information
     * for storing Appointment data and initializes the AppointmentMapper.
     */
    public AppointmentRepository() {
        super(CSV_FILE, CSV_HEADER, new AppointmentMapper());
    }

    /**
     * Retrieves all Appointment records from the CSV file.
     *
     * @return An array of all Appointment objects in the repository.
     */
    @Override
    public Appointment[] findAll() {
        return super.findAll(Appointment.class);
    }

    /**
     * Finds and returns multiple Appointment records associated with the specified doctor ID.
     *
     * @param doctorId The doctor ID to search for.
     * @return An array of Appointment objects associated with the specified doctor ID.
     */
    public Appointment[] findManyByDoctorId(String doctorId) {
        List<String> lines = this.fileManager.findLinesByColumnValue("doctorId", doctorId);
        return super.mapLines(lines, Appointment.class);
    }
    
    /**
     * Finds and returns multiple Appointment records associated with the specified patient ID.
     *
     * @param patientId The patient ID to search for.
     * @return An array of Appointment objects associated with the specified patient ID.
     */
    public Appointment[] findManyByPatientId(String patientId) {
        List<String> lines = this.fileManager.findLinesByColumnValue("patientId", patientId);
        return super.mapLines(lines, Appointment.class);
    }

    /**
     * Finds and returns multiple Appointment records with the specified status.
     *
     * @param status The status of the appointments to search for.
     * @return An array of Appointment objects with the specified status.
     */
    public Appointment[] findManyByStatus(AppointmentStatus status) {
        List<String> lines = this.fileManager.findLinesByColumnValue("status", status.toString());
        return super.mapLines(lines, Appointment.class);
    }
}
