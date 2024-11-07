package repository;

import model.Pharmacist;
import repository.base.CsvRepository;
import repository.mapper.PharmacistMapper;

public class PharmacistRepository extends CsvRepository<Pharmacist> {
    private static final String CSV_FILE = "data/pharmacists.csv";
    private static final String CSV_HEADER = "id,password,salt,name,age,gender,createdAt,updatedAt";

    public PharmacistRepository() {
        super(CSV_FILE, CSV_HEADER);
    }

    public Pharmacist findOne(String id) {
        String line = this.fileManager.readLine(id);
        if(line == null) return null;
        return PharmacistMapper.fromCsvString(line);
    }
}
