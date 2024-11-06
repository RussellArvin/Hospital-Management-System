package repository;

import model.Patient;
import repository.base.CsvRepository;
import repository.mapper.PatientMapper;

import java.util.ArrayList;
import java.util.List;

public class PatientRepository extends CsvRepository<Patient> {
    private static final String CSV_FILE = "data/patient.csv";
    private static final String CSV_HEADER = "id,password,name,dateOfBirth,gender,bloodType,phone,email";

    public PatientRepository() {
        super(CSV_FILE, CSV_HEADER);
    }

    public Patient findOne(String id) {
        String line = this.fileManager.readLine(id);
        if(line == null) return null;
        return PatientMapper.fromCsvString(line);
    }

    public void save(Patient patient) {
        this.fileManager.appendLine(patient.toCsvString());
    }

    public void update(Patient patient) {
        // Read all lines
        List<String> lines = this.fileManager.readAllLines();
        boolean found = false;

        // Create temporary list for updated content
        List<String> updatedLines = new ArrayList<>();
        updatedLines.add(CSV_HEADER);  // Add header

        // Update the specific patient line
        for (String line : lines) {
            if (line.startsWith(CSV_HEADER)) continue;  // Skip header

            Patient existingPatient = PatientMapper.fromCsvString(line);
            if (existingPatient.getId().equals(patient.getId())) {
                updatedLines.add(patient.toCsvString());
                found = true;
            } else {
                updatedLines.add(line);
            }
        }

        // If patient wasn't found, throw exception
        if (!found) {
            throw new RuntimeException("Patient not found for update: " + patient.getId());
        }

        // Write all lines back to file
        this.fileManager.writeAllLines(updatedLines);
    }
}