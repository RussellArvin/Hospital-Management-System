package service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.AppointmentDetail;
import model.Doctor;
import model.Patient;
import repository.DoctorRepository;
import repository.PatientRepository;

public class AppointmentScheduleService {
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private AppointmentService appointmentService;

    public AppointmentScheduleService(
        AppointmentService appointmentService,
        PatientRepository patientRepository,
        DoctorRepository doctorRepository
    ){
        this.appointmentService = appointmentService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Checks if a given time slot is available for both doctor and patient
     */
    public boolean isSlotFree(
        String doctorId,
        String patientId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
    ) {
        Doctor doctor = doctorRepository.findOne(doctorId);
        if(doctor == null){
            System.out.println("Doctor could not be found");
            return false;
        }

        Patient patient = patientRepository.findOne(patientId);
        if(patient == null){
            System.out.println("Patient could not be found");
            return false;
        }

        // Check if appointment is within doctor's working hours
        int appointmentHour = startDateTime.getHour();
        if (appointmentHour < doctor.getStartWorkHours() || appointmentHour >= doctor.getEndWorkHours()) {
            System.out.println("Appointment time is outside doctor's working hours (" + 
                String.format("%02d:00", doctor.getStartWorkHours()) + " to " + 
                String.format("%02d:00", doctor.getEndWorkHours()) + ")");
            return false;
        }

        AppointmentDetail[] doctorAppointments = appointmentService.findManyByDoctorId(doctorId);
        AppointmentDetail[] patientAppointments = appointmentService.findManyByPatientId(patientId);

        // Check if either doctor or patient has conflicting appointments
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
     * Returns an array of doctors available during the specified time slot for a given patient
     */
    public Doctor[] getAvailableDoctors(
        String patientId,  // Added patientId parameter
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
    ) {
        // Input validation
        if (patientId == null || startDateTime == null || endDateTime == null) {
            System.out.println("Input parameters cannot be null");
            return new Doctor[0];
        }
        
        if (startDateTime.isEqual(endDateTime) || startDateTime.isAfter(endDateTime)) {
            System.out.println("Invalid time slot: start time must be before end time");
            return new Doctor[0];
        }

        // Check if patient exists and is available
        Patient patient = patientRepository.findOne(patientId);
        if (patient == null) {
            System.out.println("Patient could not be found");
            return new Doctor[0];
        }

        // Check if patient is available at this time
        AppointmentDetail[] patientAppointments = appointmentService.findManyByPatientId(patientId);
        if (!isTimeSlotAvailable(patientAppointments, startDateTime, endDateTime)) {
            System.out.println("Patient has another appointment at this time");
            return new Doctor[0];
        }

        // Get all doctors and appointments
        Doctor[] allDoctors = doctorRepository.findAll();
        AppointmentDetail[] allAppointments = appointmentService.findAll();
        
        List<Doctor> availableDoctors = new ArrayList<>();
        
        // Check each doctor's availability
        for (Doctor doctor : allDoctors) {
            List<AppointmentDetail> doctorAppointments = new ArrayList<>();
            
            // Get this doctor's appointments
            for (AppointmentDetail appointment : allAppointments) {
                if (appointment.getDoctor().getId().equals(doctor.getId())) {
                    doctorAppointments.add(appointment);
                }
            }
            
            // If doctor has no conflicting appointments, add to available list
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
     * Helper method to check if a time slot is available
     */
    private boolean isTimeSlotAvailable(
        AppointmentDetail[] appointments, 
        LocalDateTime startDateTime, 
        LocalDateTime endDateTime
    ) {
        for (AppointmentDetail appointment : appointments) {
            // Check all possible overlap cases
            boolean hasOverlap = 
                // New appointment starts during existing one
                (startDateTime.isAfter(appointment.getStartDateTime()) && 
                 startDateTime.isBefore(appointment.getEndDateTime())) ||
                
                // New appointment ends during existing one
                (endDateTime.isAfter(appointment.getStartDateTime()) && 
                 endDateTime.isBefore(appointment.getEndDateTime())) ||
                
                // New appointment contains existing one
                (startDateTime.isBefore(appointment.getStartDateTime()) && 
                 endDateTime.isAfter(appointment.getEndDateTime())) ||
                
                // New appointment is contained within existing one
                (startDateTime.isAfter(appointment.getStartDateTime()) && 
                 endDateTime.isBefore(appointment.getEndDateTime())) ||
                
                // Exact same start or end time
                (startDateTime.isEqual(appointment.getStartDateTime()) || 
                 endDateTime.isEqual(appointment.getEndDateTime()));

            if (hasOverlap) {
                return false;
            }
        }
        return true;
    }
}