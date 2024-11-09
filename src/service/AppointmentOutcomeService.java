package service;

import model.Appointment;
import model.AppointmentDetail;
import model.AppointmentOutcome;
import model.AppointmentOutcomeDetail;
import model.Doctor;
import model.Patient;
import model.Prescription;
import repository.AppointmentOutcomeRepository;
import repository.AppointmentRepository;
import repository.DoctorRepository;
import repository.PatientRepository;
import repository.PrescriptionRepository;

public class AppointmentOutcomeService {
    private AppointmentOutcomeRepository appointmentOutcomeRepository;
    private PrescriptionRepository prescriptionRepository;
    private AppointmentRepository appointmentRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;

    public AppointmentOutcomeService(
        AppointmentOutcomeRepository appointmentOutcomeRepository,
        PrescriptionRepository prescriptionRepository,
        AppointmentRepository appointmentRepository,
        DoctorRepository doctorRepository,
        PatientRepository patientRepository
    ){
        this.appointmentOutcomeRepository = appointmentOutcomeRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    private AppointmentOutcomeDetail[] mapDetails(AppointmentOutcome[] outcomes){
        AppointmentOutcomeDetail[] details = new AppointmentOutcomeDetail[outcomes.length];

        for(int i = 0; i < details.length; i++){
            AppointmentOutcome outcome = outcomes[i];
            Prescription[] prescriptions = prescriptionRepository.findManyByOutcomeId(outcome.getId());
            Appointment appointment = appointmentRepository.findOne(outcome.getAppointmentId());
            Doctor doctor = doctorRepository.findOne(appointment.getDoctorId());
            Patient patient = patientRepository.findOne(appointment.getPatientId());

            details[i] = AppointmentOutcomeDetail.fromAppointmentOutcome(
                outcome, 
                prescriptions, 
                AppointmentDetail.fromAppointment(
                    appointment, 
                    doctor, 
                    patient
                )
            );
        }
        return details;
    }

    public AppointmentOutcomeDetail[] getForPatient(String patientId){
        AppointmentOutcome[] outcomes = appointmentOutcomeRepository.findManyByPatientId(patientId);
        return mapDetails(outcomes);
    }
}
