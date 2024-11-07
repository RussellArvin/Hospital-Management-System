package repository;

import model.Patient;
import repository.base.CsvRepository;
import repository.mapper.PatientMapper;

public class PatientRepository extends CsvRepository<Patient,PatientMapper> {
    private static final String CSV_FILE = "data/patients.csv";
    private static final String CSV_HEADER = "id,password,salt,name,age,dateOfBirth,gender,bloodType,phoneNumber,email,createdAt,updatedAt";

    public PatientRepository() {
        super(CSV_FILE, CSV_HEADER, new PatientMapper());
    }

    @Override
    public Patient[] findAll() {
        return super.findAll(Patient.class);
    }
}