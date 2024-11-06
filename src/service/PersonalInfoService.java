package service;

import model.Patient;
import repository.PatientRepository;
import validators.InputValidator;

public class PersonalInfoService {
    private final PatientRepository patientRepository;

    public PersonalInfoService(
        PatientRepository patientRepository
    ) {
        this.patientRepository = patientRepository;
    }

    public void update(
        String patientId,
        String phonerNumber,
        String email
    ){
        if(!InputValidator.validatePhone(phonerNumber)){
            System.out.println("Invalid Phone Number");
            return;
        }

        if(!InputValidator.validateEmail(email)){
            System.out.println("Invalid email address");
        }

        Patient patient = patientRepository.findOne(patientId);
        patient.setPhoneNumber(Integer.parseInt(phonerNumber));
        patient.setEmail(email);
        patientRepository.save(patient);
        return;
    }
}
