package service;

import model.Patient;
import repository.PatientRepository;

public class MedicalRecordService {
    private final PatientRepository patientRepository;

    public MedicalRecordService(
        PatientRepository patientRepository
    ) {
        this.patientRepository = patientRepository;
    }

    public void view(Patient patient){

    }

    public void update(
        Patient patient,
        String phonerNumber,
        String email
    ){
        patient.setPhoneNumber(Integer.parseInt(phonerNumber));
        patient.setEmail(email);
        patientRepository.update(patient);
        return;
    }
}
