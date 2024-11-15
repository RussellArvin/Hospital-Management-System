package service;

import enums.AppointmentStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import model.Appointment;
import model.AppointmentDetail;
import model.Doctor;
import model.Patient;
import repository.AppointmentRepository;
import repository.DoctorRepository;
import repository.PatientRepository;

/**
 * The AppointmentService class manages appointment-related operations such as creation,
 * rescheduling, status updates, and fetching specific appointment details.
 * 
 * @author Tan Jou Yuan
 * @version 1.0
 */
public class AppointmentService {
    private AppointmentRepository appointmentRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;

    /**
     * Constructs an AppointmentService with the required repositories.
     *
     * @param appointmentRepository The repository for appointment data.
     * @param doctorRepository      The repository for doctor data.
     * @param patientRepository     The repository for patient data.
     */
    public AppointmentService(
        AppointmentRepository appointmentRepository,
        DoctorRepository doctorRepository,
        PatientRepository patientRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * Retrieves the ID of a doctor by their name.
     *
     * @param name The name of the doctor.
     * @return The ID of the doctor, or null if not found.
     */
    public String getDoctorId(String name) {
        try {
            Doctor doctor = doctorRepository.findOneByName(name);
            if (doctor == null) return null;
            return doctor.getId();
        } catch (Exception e) {
            System.out.println("Something went wrong when finding the doctor");
            return null;
        }
    }

    /**
     * Reschedules an appointment to a new time.
     *
     * @param appointmentId  The ID of the appointment.
     * @param startDateTime  The new start time.
     * @param endDateTime    The new end time.
     * @return null if successful, otherwise an error message.
     */
    public String rescheduleAppointment(
        String appointmentId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
    ) {
        try {
            Appointment appointment = appointmentRepository.findOne(appointmentId);
            if (appointment == null) return "Unable to find appointment";

            appointment.setStartDateTime(startDateTime);
            appointment.setEndDateTime(endDateTime);
            appointmentRepository.update(appointment);
            return null;
        } catch (Exception e) {
            return "Something went wrong when rescheduling appointment";
        }
    }

    /**
     * Creates a new appointment.
     *
     * @param patientId     The ID of the patient.
     * @param doctorId      The ID of the doctor.
     * @param startDateTime The start time of the appointment.
     * @param endDateTime   The end time of the appointment.
     * @return null if successful, otherwise an error message.
     */
    public String createAppointment(
        String patientId,
        String doctorId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
    ) {
        try {
            Doctor doctor = doctorRepository.findOne(doctorId);
            if (doctor == null) {
                return "Doctor could not be found";
            }

            Patient patient = patientRepository.findOne(patientId);
            if (patient == null) {
                return "Patient could not be found";
            }

            Appointment appointment = new Appointment(doctorId, patientId, startDateTime, endDateTime);
            appointmentRepository.save(appointment);
            return null;
        } catch (Exception e) {
            return "Something went wrong when creating an appointment";
        }
    }

    /**
     * Updates the status of an appointment.
     *
     * @param appointmentId The ID of the appointment.
     * @param status        The new status to set.
     * @param cancelReason  The reason for cancellation, if applicable.
     * @return null if successful, otherwise an error message.
     */
    public String setStatus(
        String appointmentId,
        AppointmentStatus status,
        String cancelReason
    ) {
        try {
            Appointment appointment = appointmentRepository.findOne(appointmentId);
            if (appointment == null) {
                return "Cant find appointment to update";
            }

            appointment.setStatus(status);
            if (status == AppointmentStatus.DOCTOR_CANCELLED) appointment.setCancelReason(cancelReason);
            appointmentRepository.update(appointment);
            return null;
        } catch (Exception e) {
            return "Something went wrong when updating the appointment";
        }
    }

    /**
     * Finds today's confirmed appointments.
     *
     * @return An array of today's confirmed appointments.
     */
    public AppointmentDetail[] findToday() {
        Appointment[] appointments = appointmentRepository.findAll();
        Appointment[] todayAppointments = Arrays.stream(appointments)
            .filter(appointment -> appointment.getStartDateTime().toLocalDate() == LocalDate.now() &&
                                    appointment.getStatus() == AppointmentStatus.CONFIRMED)
            .toArray(AppointmentDetail[]::new);

        return mapDetails(todayAppointments);
    }

    /**
     * Finds all completed appointments.
     *
     * @return An array of completed appointments.
     */
    public AppointmentDetail[] findCompleted() {
        return this.findManyByStatus(AppointmentStatus.COMPLETED);
    }

    /**
     * Finds pending or confirmed appointments by patient ID.
     *
     * @param patientId The ID of the patient.
     * @return An array of pending or confirmed appointments for the patient.
     */
    public AppointmentDetail[] findPendingCompletedByPatientId(String patientId) {
        AppointmentDetail[] appointments = this.findManyByPatientId(patientId);

        return Arrays.stream(appointments)
            .filter(appointment -> appointment.getStatus() == AppointmentStatus.REQUESTED ||
                                   appointment.getStatus() == AppointmentStatus.CONFIRMED)
            .toArray(AppointmentDetail[]::new);
    }

    /**
     * Finds confirmed appointments by patient ID.
     *
     * @param patientId The ID of the patient.
     * @return An array of confirmed appointments for the patient.
     */
    public AppointmentDetail[] findConfirmedByPatientId(String patientId) {
        AppointmentDetail[] appointments = this.findManyByPatientId(patientId);

        return Arrays.stream(appointments)
            .filter(appointment -> appointment.getStatus() == AppointmentStatus.CONFIRMED)
            .toArray(AppointmentDetail[]::new);
    }

    /**
     * Finds requested appointments by doctor ID.
     *
     * @param doctorId The ID of the doctor.
     * @return An array of requested appointments for the doctor.
     */
    public AppointmentDetail[] findRequestedByDoctor(String doctorId) {
        AppointmentDetail[] appointments = this.findManyByDoctorId(doctorId);

        return Arrays.stream(appointments)
            .filter(appointment -> appointment.getStatus() == AppointmentStatus.REQUESTED)
            .toArray(AppointmentDetail[]::new);
    }

    /**
     * Finds completed appointments by doctor ID.
     *
     * @param doctorId The ID of the doctor.
     * @return An array of completed appointments for the doctor.
     */
    public AppointmentDetail[] findCompletedByDoctor(String doctorId) {
        AppointmentDetail[] appointments = this.findManyByDoctorId(doctorId);

        return Arrays.stream(appointments)
            .filter(appointment -> appointment.getStatus() == AppointmentStatus.COMPLETED)
            .toArray(AppointmentDetail[]::new);
    }

    /**
     * Finds appointments by doctor ID.
     *
     * @param doctorId The ID of the doctor.
     * @return An array of appointments for the doctor.
     */
    public AppointmentDetail[] findManyByDoctorId(String doctorId) {
        Appointment[] appointments = appointmentRepository.findManyByDoctorId(doctorId);
        return mapDetails(appointments);
    }

    /**
     * Finds appointments for a doctor on a specific date.
     *
     * @param doctorId The ID of the doctor.
     * @param date     The specific date.
     * @return An array of appointments for the doctor on the specified date.
     */
    public AppointmentDetail[] findDoctorAppointmentsByDate(String doctorId, LocalDate date) {
        AppointmentDetail[] appointments = this.findManyByDoctorId(doctorId);

        return Arrays.stream(appointments)
            .filter(appointment ->
                (appointment.getStatus() == AppointmentStatus.REQUESTED ||
                appointment.getStatus() == AppointmentStatus.CONFIRMED) &&
                appointment.getStartDateTime().toLocalDate().equals(date)
            )
            .toArray(AppointmentDetail[]::new);
    }

    /**
     * Maps an Appointment to an AppointmentDetail.
     *
     * @param appointment The appointment to map.
     * @return The mapped AppointmentDetail.
     */
    private AppointmentDetail mapDetail(Appointment appointment) {
        Doctor doctor = doctorRepository.findOne(appointment.getDoctorId());
        Patient patient = patientRepository.findOne(appointment.getPatientId());

        return AppointmentDetail.fromAppointment(appointment, doctor, patient);
    }

    /**
     * Maps an array of Appointments to an array of AppointmentDetails.
     *
     * @param appointments The appointments to map.
     * @return The mapped AppointmentDetails.
     */
    private AppointmentDetail[] mapDetails(Appointment[] appointments) {
        AppointmentDetail[] details = new AppointmentDetail[appointments.length];

        for (int i = 0; i < appointments.length; i++) {
            details[i] = mapDetail(appointments[i]);
        }

        return details;
    }

    /**
     * Finds all appointments.
     *
     * @return An array of all appointments.
     */
    public AppointmentDetail[] findAll() {
        Appointment[] appointments = appointmentRepository.findAll();
        return mapDetails(appointments);
    }

    /**
     * Finds appointments by status.
     *
     * @param status The status to filter by.
     * @return An array of appointments with the specified status.
     */
    private AppointmentDetail[] findManyByStatus(AppointmentStatus status) {
        Appointment[] appointments = appointmentRepository.findManyByStatus(status);
        return mapDetails(appointments);
    }

    /**
     * Finds appointments by patient ID.
     *
     * @param patientId The ID of the patient.
     * @return An array of appointments for the patient.
     */
    public AppointmentDetail[] findManyByPatientId(String patientId) {
        Appointment[] appointments = appointmentRepository.findManyByPatientId(patientId);
        return mapDetails(appointments);
    }
}
