package repository;

import model.Patient;
import repository.base.CsvRepository;
import repository.mapper.PatientMapper;

public class PatientRepository extends CsvRepository<Patient> {
    private static final String CSV_FILE = "data/patients.csv";
    private static final String CSV_HEADER = "id,password,name,age,dateOfBirth,gender,bloodType,phoneNumber,email,createdAt,updatedAt";

    public PatientRepository() {
        super(CSV_FILE, CSV_HEADER);
    }

    public Patient findOne(String id) {
        String line = this.fileManager.readLine(id);
        if(line == null) return null;
        return PatientMapper.fromCsvString(line);
    }

}