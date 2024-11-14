package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import enums.AppointmentStatus;
import model.Appointment;
import model.AppointmentDetail;
import model.Doctor;
import model.Patient;
import repository.AppointmentRepository;
import repository.DoctorRepository;
import repository.PatientRepository;

public class AppointmentService {
    private AppointmentRepository appointmentRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    
    public AppointmentService(
        AppointmentRepository appointmentRepository,
        DoctorRepository doctorRepository,
        PatientRepository patientRepository
    ){
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public String getDoctorId(String name){
        try{
            Doctor doctor = doctorRepository.findOneByName(name);
            if(doctor == null) return null;
            return doctor.getId();
        } catch(Exception e){
            System.out.println("Something went wrong when finding the doctor");
            return null;
        }
    }

    public String rescheduleAppointment(
        String appointmentId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
    ){
        try{
            Appointment appointment = appointmentRepository.findOne(appointmentId);
            if(appointment == null) return "Unable to find appointment";

            appointment.setStartDateTime(startDateTime);
            appointment.setEndDateTime(endDateTime);
            appointmentRepository.update(appointment);
            return null;
        }catch(Exception e){
            return "Something went wrong when rescheduling appointment";
        }
    }

    public String createAppointment(
        String patientId,
        String doctorId,
        LocalDateTime startDateTime,
        LocalDateTime enDateTime
    ){
        try{
            Doctor doctor = doctorRepository.findOne(doctorId);
            if(doctor == null){
                return "Doctor could not be found";
            }

            Patient patient = patientRepository.findOne(patientId);
            if(patient == null){
                return "Patient could not be found";
            }

            Appointment appointment = new Appointment(doctorId, patientId, startDateTime, enDateTime);
            appointmentRepository.save(appointment);
            return null;
        } catch(Exception e){
            return "Something went wrong when creating an appointment";
        }
    }

    public String setStatus(
        String appointmentId,
        AppointmentStatus status,
        String cancelReason
    ) {
        try{
            Appointment appointment = appointmentRepository.findOne(appointmentId);
            if(appointment == null) {
                return "Cant find appointment to update";
            }
    
            appointment.setStatus(status);
            if(status == AppointmentStatus.DOCTOR_CANCELLED) appointment.setCancelReason(cancelReason);
            appointmentRepository.update(appointment);
            return null;
        }catch(Exception e){
            return "Something went wrong when updating the appointment";
        }
    }

    public AppointmentDetail[] findToday(){
        Appointment[] appointments = appointmentRepository.findAll();
        Appointment[] todayAppointments = Arrays.stream(appointments)
            .filter(appointment -> appointment.getStartDateTime().toLocalDate() == LocalDate.now() && 
                                appointment.getStatus() == AppointmentStatus.CONFIRMED)
            .toArray(AppointmentDetail[]::new);

        return mapDetails(todayAppointments);
    }

    public AppointmentDetail[] findCompleted(){
        return this.findManyByStatus(AppointmentStatus.COMPLETED);
    }

    public AppointmentDetail[] findPendingCompletedByPatientId(String patientId){
        AppointmentDetail[] appointments = this.findManyByPatientId(patientId);

        return Arrays.stream(appointments)
        .filter(appointment -> appointment.getStatus() == AppointmentStatus.REQUESTED || 
                             appointment.getStatus() == AppointmentStatus.CONFIRMED)
        .toArray(AppointmentDetail[]::new);
    }

    public AppointmentDetail[] findConfirmedByPatientId(String patientId){
        AppointmentDetail[] appointments = this.findManyByPatientId(patientId);

        return Arrays.stream(appointments)
        .filter(appointment -> appointment.getStatus() == AppointmentStatus.CONFIRMED)
        .toArray(AppointmentDetail[]::new);
    }

    public AppointmentDetail[] findRequestedByDoctor(String doctorId) {
        AppointmentDetail[] appointments = this.findManyByDoctorId(doctorId);
        
        return Arrays.stream(appointments)
            .filter(appointment -> appointment.getStatus() == AppointmentStatus.REQUESTED)
            .toArray(AppointmentDetail[]::new);
    }

    public AppointmentDetail[] findCompletedByDoctor(String doctorId) {
        AppointmentDetail[] appointments = this.findManyByDoctorId(doctorId);
        
        return Arrays.stream(appointments)
            .filter(appointment -> appointment.getStatus() == AppointmentStatus.COMPLETED)
            .toArray(AppointmentDetail[]::new);
    }

    public AppointmentDetail[] findManyByDoctorId(String doctorId){
        Appointment[] appointments = appointmentRepository.findManyByDoctorId(doctorId);
        return mapDetails(appointments);

    }

    public AppointmentDetail[] findDoctorAppointmentsByDate(String doctorId, LocalDate date){
        AppointmentDetail[] appointments = this.findManyByDoctorId(doctorId);

        return Arrays.stream(appointments)
            .filter(appointment -> 
                (appointment.getStatus() == AppointmentStatus.REQUESTED || 
                appointment.getStatus() == AppointmentStatus.CONFIRMED) &&
                appointment.getStartDateTime().toLocalDate().equals(date)
            )
            .toArray(AppointmentDetail[]::new);
    }

    private AppointmentDetail mapDetail(Appointment appointment){
        Doctor doctor = doctorRepository.findOne(appointment.getDoctorId());
        Patient patient = patientRepository.findOne(appointment.getPatientId());

        //Create object from static factory method
        return AppointmentDetail.fromAppointment(
            appointment, 
            doctor, 
            patient
        );
    }

    private AppointmentDetail[] mapDetails(Appointment[] appointments){
        AppointmentDetail[] details = new AppointmentDetail[appointments.length];

        for(int i = 0; i < appointments.length; i++){
            details[i] = mapDetail(appointments[i]);
        }

        return details;
    }

    public AppointmentDetail[] findAll(){
        Appointment[] appointments = appointmentRepository.findAll();
        return mapDetails(appointments);
    }

    private AppointmentDetail[] findManyByStatus(AppointmentStatus status){
        Appointment[] appointments = appointmentRepository.findManyByStatus(status);
        return mapDetails(appointments);

    }

    public AppointmentDetail[] findManyByPatientId(String patientId){
        Appointment[] appointments = appointmentRepository.findManyByPatientId(patientId);
        return mapDetails(appointments);
    }
}
