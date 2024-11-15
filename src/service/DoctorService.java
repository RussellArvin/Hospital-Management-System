package service;

import enums.AppointmentStatus;
import java.util.Arrays;
import java.util.Objects;
import model.Appointment;
import model.Doctor;
import model.Patient;
import repository.AppointmentRepository;
import repository.DoctorRepository;
import repository.PatientRepository;

/**
 * The DoctorService class provides functionality for managing doctor-related operations,
 * including retrieving patients associated with a doctor and setting the availability of a doctor.
 * 
 * @author Celeste Ho 
 * @version 1.0
 */
public class DoctorService {
    private DoctorRepository doctorRepository;
    private AppointmentRepository appointmentRepository;
    private PatientRepository patientRepository;

    /**
     * Constructs a DoctorService with the required repositories.
     *
     * @param doctorRepository     The repository for doctor data.
     * @param appointmentRepository The repository for appointment data.
     * @param patientRepository     The repository for patient data.
     */
    public DoctorService(
        DoctorRepository doctorRepository,
        AppointmentRepository appointmentRepository,
        PatientRepository patientRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * Retrieves the unique list of patients who have appointments with a specific doctor.
     *
     * @param doctorId The ID of the doctor.
     * @return An array of Patient objects associated with the doctor.
     */
    public Patient[] getDoctorPatients(String doctorId) {
        Appointment[] appointments = appointmentRepository.findManyByDoctorId(doctorId);
        appointments = Arrays.stream(appointments)
            .filter(appointment -> 
                appointment.getStatus() != AppointmentStatus.DOCTOR_CANCELLED && 
                appointment.getStatus() != AppointmentStatus.PATIENT_CANCELLED
            )
            .toArray(Appointment[]::new);
        
        // Get unique patient IDs and fetch patient details
        return Arrays.stream(appointments)
            .map(Appointment::getPatientId)  
            .distinct()                      
            .map(patientRepository::findOne) 
            .filter(Objects::nonNull)        
            .toArray(Patient[]::new);     
    }

    /**
     * Sets the availability of a doctor by updating their working hours.
     *
     * @param doctorId       The ID of the doctor.
     * @param startWorkHours The start of the doctor's working hours.
     * @param endWorkHours   The end of the doctor's working hours.
     * @return null if successful, otherwise an error message.
     */
    public String setAvailability(
        String doctorId,
        int startWorkHours,
        int endWorkHours
    ) {
        try {
            Doctor doctor = doctorRepository.findOne(doctorId);
            if (doctor == null) return "Unable to find doctor";

            doctor.setWorkHours(startWorkHours, endWorkHours);
            doctorRepository.update(doctor);
            return null;
        } catch (Exception e) {
            return "Something went wrong when setting doctor's availability";
        }
    }
}
