package service;

import enums.AppointmentServiceType;
import enums.AppointmentStatus;
import java.util.Arrays;
import model.Appointment;
import model.AppointmentDetail;
import model.AppointmentOutcome;
import model.AppointmentOutcomeDetail;
import model.BaseEntity;
import model.Doctor;
import model.Medicine;
import model.Patient;
import model.PendingPrescription;
import model.Prescription;
import model.PrescriptionWithMedicine;
import repository.AppointmentOutcomeRepository;
import repository.AppointmentRepository;
import repository.DoctorRepository;
import repository.MedicineRepository;
import repository.PatientRepository;
import repository.PrescriptionRepository;


/**
 * The AppointmentOutcomeService class provides methods for managing appointment outcomes,
 * including creating outcomes, dispensing medicines, and retrieving detailed information.
 * 
 * @author Natalyn Pong 
 * @version 1.0
 */
public class AppointmentOutcomeService {
    private AppointmentOutcomeRepository appointmentOutcomeRepository;
    private PrescriptionRepository prescriptionRepository;
    private AppointmentRepository appointmentRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private MedicineRepository medicineRepository;

    /**
     * Constructs an AppointmentOutcomeService with the necessary repositories.
     *
     * @param appointmentOutcomeRepository Repository for AppointmentOutcome data.
     * @param prescriptionRepository       Repository for Prescription data.
     * @param appointmentRepository        Repository for Appointment data.
     * @param doctorRepository             Repository for Doctor data.
     * @param patientRepository            Repository for Patient data.
     * @param medicineRepository           Repository for Medicine data.
     */
    public AppointmentOutcomeService(
        AppointmentOutcomeRepository appointmentOutcomeRepository,
        PrescriptionRepository prescriptionRepository,
        AppointmentRepository appointmentRepository,
        DoctorRepository doctorRepository,
        PatientRepository patientRepository,
        MedicineRepository medicineRepository
    ) {
        this.appointmentOutcomeRepository = appointmentOutcomeRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.medicineRepository = medicineRepository;
    }

    /**
     * Maps an AppointmentOutcome to an AppointmentOutcomeDetail, including associated prescriptions and appointment details.
     *
     * @param outcome The AppointmentOutcome to map.
     * @return The mapped AppointmentOutcomeDetail.
     */
    private AppointmentOutcomeDetail mapDetail(AppointmentOutcome outcome) {
        Prescription[] rawPrescriptions = prescriptionRepository.findManyByOutcomeId(outcome.getId());
        PrescriptionWithMedicine[] prescriptions = new PrescriptionWithMedicine[rawPrescriptions.length];

        for(int i =0; i < prescriptions.length; i ++){
            Prescription rawPrescription = rawPrescriptions[i];
            Medicine medicine = medicineRepository.findOne(rawPrescription.getMedicineId());
            prescriptions[i] = PrescriptionWithMedicine.fromPrescription(rawPrescription, medicine);
        }


        Appointment appointment = appointmentRepository.findOne(outcome.getAppointmentId());
        Doctor doctor = doctorRepository.findOne(appointment.getDoctorId());
        Patient patient = patientRepository.findOne(appointment.getPatientId());

        return AppointmentOutcomeDetail.fromAppointmentOutcome(
            outcome, 
            prescriptions, 
            AppointmentDetail.fromAppointment(
                appointment, 
                doctor, 
                patient
            )
        );
    }

    /**
     * Maps multiple AppointmentOutcome records to AppointmentOutcomeDetail objects.
     *
     * @param outcomes An array of AppointmentOutcome records to map.
     * @return An array of mapped AppointmentOutcomeDetail objects.
     */
    private AppointmentOutcomeDetail[] mapDetails(AppointmentOutcome[] outcomes) {
        AppointmentOutcomeDetail[] details = new AppointmentOutcomeDetail[outcomes.length];

        for(int i = 0; i < details.length; i++){
            AppointmentOutcome outcome = outcomes[i];
            details[i] = mapDetail(outcome);
        }
        return details;    }

    /**
     * Retrieves completed AppointmentOutcomeDetail records for a specific patient.
     *
     * @param patientId The ID of the patient.
     * @return An array of AppointmentOutcomeDetail objects for the patient's completed appointments.
     */
    public AppointmentOutcomeDetail[] findAllPatientCompleted(String patientId) {
        Appointment[] appointments = appointmentRepository.findManyByPatientId(patientId);
        Appointment[] completedAppointments = Arrays.stream(appointments)
            .filter(appointment -> appointment.getStatus() == AppointmentStatus.COMPLETED)
            .toArray(Appointment[]::new);

        AppointmentOutcomeDetail[] outcomes = new AppointmentOutcomeDetail[completedAppointments.length];

        for(int i = 0; i < outcomes.length; i++){
            String appointmentId  = completedAppointments[i].getId();
            AppointmentOutcome rawOutcome = appointmentOutcomeRepository.findOneByAppointmentId(appointmentId);
            outcomes[i] = mapDetail(rawOutcome);
        }
        return outcomes;    }

    /**
     * Retrieves all confirmed AppointmentOutcomeDetail records.
     *
     * @return An array of confirmed AppointmentOutcomeDetail objects.
     */
    public AppointmentOutcomeDetail[] findAllConfirmed() {
        Appointment[] appointments = appointmentRepository.findManyByStatus(AppointmentStatus.COMPLETED);
        AppointmentOutcomeDetail[] outcomes = new AppointmentOutcomeDetail[appointments.length];

        for(int i = 0; i < outcomes.length; i++){
            String appointmentId  = appointments[i].getId();
            AppointmentOutcome rawOutcome = appointmentOutcomeRepository.findOneByAppointmentId(appointmentId);
            outcomes[i] = mapDetail(rawOutcome);

        }

        return outcomes;    }

    /**
     * Creates a new AppointmentOutcome with prescriptions and updates the appointment status.
     *
     * @param appointmentId     The ID of the appointment.
     * @param serviceType       The type of service provided during the appointment.
     * @param consultationNotes The consultation notes.
     * @param rawPrescriptions  An array of pending prescriptions.
     * @return null if successful, or an error message if creation fails.
     */
    public String createOutcome(
        String appointmentId,
        AppointmentServiceType serviceType,
        String consultationNotes,
        PendingPrescription[] rawPrescriptions
    ) {
        try{
            String appointmentOutcomeId = BaseEntity.generateUUID();
            Prescription[] prescriptions = new Prescription[rawPrescriptions.length];

            Appointment appointment = appointmentRepository.findOne(appointmentId);
            if(appointment == null) return "Unable to find appointment";

            Patient patient = patientRepository.findOne(appointment.getPatientId());
            if(patient == null) return "Unable to find patient";
            
            //Check existence of medicine
            for(int i = 0; i < rawPrescriptions.length; i++){
                Medicine medicine = medicineRepository.findOne(rawPrescriptions[i].getMedicineId());
                if(medicine == null) return "Medicine: " + rawPrescriptions[i].getMedicineId() + " cannot be found";
                prescriptions[i] = new Prescription(appointmentOutcomeId, rawPrescriptions[i]);
            }

            AppointmentOutcome outcome = new AppointmentOutcome(appointmentOutcomeId, appointment.getPatientId(), appointmentId, serviceType, consultationNotes);
            appointment.setStatus(AppointmentStatus.COMPLETED);
            appointmentOutcomeRepository.save(outcome);
            appointmentRepository.update(appointment);
            prescriptionRepository.saveMany(prescriptions);
            return null;
        } catch(Exception e){
            System.out.println(e.toString());
            return "Something went wrong when creating an appointment outcome";
        }    
    }

    /**
     * Dispenses medicine for a specified prescription and updates inventory and prescription status.
     *
     * @param prescriptionId The ID of the prescription to dispense.
     * @return null if successful, or an error message if dispensing fails.
     */
    public String dispenseMedicine(String prescriptionId) {
        try{
            Prescription prescription = prescriptionRepository.findOne(prescriptionId);
            if(prescription == null) return "Unable to find prescription";

            Medicine medicine = medicineRepository.findOne(prescription.getMedicineId());
            if(medicine == null) return "Unable to find medicine";

            int usedAmount =prescription.getAmount();
            int existingStock = medicine.getStock();

            medicine.setStock(existingStock - usedAmount);
            prescription.dispense();

            medicineRepository.update(medicine);
            prescriptionRepository.update(prescription);
            return null;
        } catch(Exception e){
            return "Something went wrong when dispensing medicine";
        }    }

    /**
     * Retrieves all AppointmentOutcomeDetail records for a specific patient.
     *
     * @param patientId The ID of the patient.
     * @return An array of AppointmentOutcomeDetail objects for the patient.
     */
    public AppointmentOutcomeDetail[] getForPatient(String patientId) {
        AppointmentOutcome[] outcomes = appointmentOutcomeRepository.findManyByPatientId(patientId);
        return mapDetails(outcomes);
    }
}
