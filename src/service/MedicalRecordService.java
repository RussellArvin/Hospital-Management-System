package service;

import model.Patient;
import repository.PatientRepository;

/**
 * The MedicalRecordService class manages operations on patient medical records,
 * including viewing and updating patient contact information.
 * 
 * @author Lim Jun Howe 
 * @version 1.0
 */
public class MedicalRecordService {
    private final PatientRepository patientRepository;

    /**
     * Constructs a MedicalRecordService with the specified PatientRepository.
     *
     * @param patientRepository Repository for managing patient data.
     */
    public MedicalRecordService(
        PatientRepository patientRepository
    ) {
        this.patientRepository = patientRepository;
    }



    /**
     * Updates the contact information for a given patient.
     *
     * @param patient      The Patient whose information is being updated.
     * @param phoneNumber  The new phone number for the patient.
     * @param email        The new email address for the patient.
     */
    public void update(
        Patient patient,
        String phonerNumber,
        String email
    ) {
        patient.setPhoneNumber(Integer.parseInt(phonerNumber));
        patient.setEmail(email);
        patientRepository.update(patient);
        return;
    }
}
