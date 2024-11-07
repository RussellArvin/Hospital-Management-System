package repository;

import model.Pharmacist;
import repository.base.CsvRepository;
import repository.mapper.PharmacistMapper;

public class PharmacistRepository extends CsvRepository<Pharmacist, PharmacistMapper> {
    private static final String CSV_FILE = "data/pharmacists.csv";
    private static final String CSV_HEADER = "id,password,salt,name,age,gender,createdAt,updatedAt";

    public PharmacistRepository() {
        super(CSV_FILE, CSV_HEADER, new PharmacistMapper());
    }

    @Override
    public Pharmacist[] findAll() {
        return super.findAll(Pharmacist.class);
    }
}
