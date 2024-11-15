package service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.AppointmentDetail;
import model.Doctor;
import model.Patient;
import repository.DoctorRepository;
import repository.PatientRepository;

/**
 * The AppointmentScheduleService class provides functionality for checking the availability
 * of time slots for appointments and retrieving available doctors for a given time frame.
 * @author Lim Jun Howe 
 * @version 1.0
 */
public class AppointmentScheduleService {
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private AppointmentService appointmentService;

    /**
     * Constructs an AppointmentScheduleService with the necessary dependencies.
     *
     * @param appointmentService The service to manage appointments.
     * @param patientRepository  The repository for managing patient data.
     * @param doctorRepository   The repository for managing doctor data.
     */
    public AppointmentScheduleService(
        AppointmentService appointmentService,
        PatientRepository patientRepository,
        DoctorRepository doctorRepository
    ) {
        this.appointmentService = appointmentService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Checks if a given time slot is available for both the doctor and the patient.
     *
     * @param doctorId       The ID of the doctor.
     * @param patientId      The ID of the patient.
     * @param startDateTime  The start time of the appointment.
     * @param endDateTime    The end time of the appointment.
     * @return true if the time slot is free, otherwise false.
     */
    public boolean isSlotFree(
        String doctorId,
        String patientId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
    ) {
        Doctor doctor = doctorRepository.findOne(doctorId);
        if (doctor == null) {
            System.out.println("Doctor could not be found");
            return false;
        }

        Patient patient = patientRepository.findOne(patientId);
        if (patient == null) {
            System.out.println("Patient could not be found");
            return false;
        }

        int appointmentHour = startDateTime.getHour();
        if (appointmentHour < doctor.getStartWorkHours() || appointmentHour >= doctor.getEndWorkHours()) {
            System.out.println("Appointment time is outside doctor's working hours (" + 
                String.format("%02d:00", doctor.getStartWorkHours()) + " to " + 
                String.format("%02d:00", doctor.getEndWorkHours()) + ")");
            return false;
        }

        AppointmentDetail[] doctorAppointments = appointmentService.findManyByDoctorId(doctorId);
        AppointmentDetail[] patientAppointments = appointmentService.findManyByPatientId(patientId);

        if (!isTimeSlotAvailable(doctorAppointments, startDateTime, endDateTime)) {
            System.out.println("Doctor is not available at this time");
            return false;
        }

        if (!isTimeSlotAvailable(patientAppointments, startDateTime, endDateTime)) {
            System.out.println("Patient has another appointment at this time");
            return false;
        }

        return true;
    }

    /**
     * Retrieves an array of doctors who are available during the specified time slot for a given patient.
     *
     * @param patientId     The ID of the patient.
     * @param startDateTime The start time of the appointment.
     * @param endDateTime   The end time of the appointment.
     * @return An array of available doctors.
     */
    public Doctor[] getAvailableDoctors(
        String patientId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
    ) {
        if (patientId == null || startDateTime == null || endDateTime == null) {
            System.out.println("Input parameters cannot be null");
            return new Doctor[0];
        }

        if (startDateTime.isEqual(endDateTime) || startDateTime.isAfter(endDateTime)) {
            System.out.println("Invalid time slot: start time must be before end time");
            return new Doctor[0];
        }

        Patient patient = patientRepository.findOne(patientId);
        if (patient == null) {
            System.out.println("Patient could not be found");
            return new Doctor[0];
        }

        AppointmentDetail[] patientAppointments = appointmentService.findManyByPatientId(patientId);
        if (!isTimeSlotAvailable(patientAppointments, startDateTime, endDateTime)) {
            System.out.println("Patient has another appointment at this time");
            return new Doctor[0];
        }

        Doctor[] allDoctors = doctorRepository.findAll();
        AppointmentDetail[] allAppointments = appointmentService.findAll();

        List<Doctor> availableDoctors = new ArrayList<>();
        for (Doctor doctor : allDoctors) {
            List<AppointmentDetail> doctorAppointments = new ArrayList<>();
            for (AppointmentDetail appointment : allAppointments) {
                if (appointment.getDoctor().getId().equals(doctor.getId())) {
                    doctorAppointments.add(appointment);
                }
            }

            if (isTimeSlotAvailable(
                doctorAppointments.toArray(new AppointmentDetail[0]),
                startDateTime,
                endDateTime)) {
                availableDoctors.add(doctor);
            }
        }

        return availableDoctors.toArray(new Doctor[0]);
    }

    /**
     * Helper method to check if a time slot is available for a given set of appointments.
     *
     * @param appointments   An array of AppointmentDetail objects to check against.
     * @param startDateTime  The start time of the new appointment.
     * @param endDateTime    The end time of the new appointment.
     * @return true if the time slot is available, otherwise false.
     */
    private boolean isTimeSlotAvailable(
        AppointmentDetail[] appointments,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
    ) {
        for (AppointmentDetail appointment : appointments) {
            boolean hasOverlap = 
                (startDateTime.isAfter(appointment.getStartDateTime()) && 
                 startDateTime.isBefore(appointment.getEndDateTime())) ||
                (endDateTime.isAfter(appointment.getStartDateTime()) && 
                 endDateTime.isBefore(appointment.getEndDateTime())) ||
                (startDateTime.isBefore(appointment.getStartDateTime()) && 
                 endDateTime.isAfter(appointment.getEndDateTime())) ||
                (startDateTime.isAfter(appointment.getStartDateTime()) && 
                 endDateTime.isBefore(appointment.getEndDateTime())) ||
                (startDateTime.isEqual(appointment.getStartDateTime()) || 
                 endDateTime.isEqual(appointment.getEndDateTime()));

            if (hasOverlap) {
                return false;
            }
        }
        return true;
    }
}
