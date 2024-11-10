package service;

import enums.AppointmentServiceType;
import enums.AppointmentStatus;
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

public class AppointmentOutcomeService {
    private AppointmentOutcomeRepository appointmentOutcomeRepository;
    private PrescriptionRepository prescriptionRepository;
    private AppointmentRepository appointmentRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private MedicineRepository medicineRepository;

    public AppointmentOutcomeService(
        AppointmentOutcomeRepository appointmentOutcomeRepository,
        PrescriptionRepository prescriptionRepository,
        AppointmentRepository appointmentRepository,
        DoctorRepository doctorRepository,
        PatientRepository patientRepository,
        MedicineRepository medicineRepository
    ){
        this.appointmentOutcomeRepository = appointmentOutcomeRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.medicineRepository = medicineRepository;
    }

    private AppointmentOutcomeDetail mapDetail(AppointmentOutcome outcome){
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

    private AppointmentOutcomeDetail[] mapDetails(AppointmentOutcome[] outcomes){
        AppointmentOutcomeDetail[] details = new AppointmentOutcomeDetail[outcomes.length];

        for(int i = 0; i < details.length; i++){
            AppointmentOutcome outcome = outcomes[i];
            details[i] = mapDetail(outcome);
        }
        return details;
    }

    public AppointmentOutcomeDetail[] findAllConfirmed(){
        Appointment[] appointments = appointmentRepository.findManyByStatus(AppointmentStatus.COMPLETED);
        AppointmentOutcomeDetail[] outcomes = new AppointmentOutcomeDetail[appointments.length];

        for(int i = 0; i < outcomes.length; i++){
            String appointmentId  = appointments[i].getId();
            AppointmentOutcome rawOutcome = appointmentOutcomeRepository.findOneByAppointmentId(appointmentId);
            outcomes[i] = mapDetail(rawOutcome);

        }

        return outcomes;
    }

    public String createOutcome(
        String appointmentId,
        AppointmentServiceType serviceType,
        String consultationNotes,
        PendingPrescription[] rawPrescriptions
    ){
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

    public String dispenseMedicine(
        String prescriptionId
    ) {
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
        }
    }

    public AppointmentOutcomeDetail[] getForPatient(String patientId){
        AppointmentOutcome[] outcomes = appointmentOutcomeRepository.findManyByPatientId(patientId);
        return mapDetails(outcomes);
    }
}
