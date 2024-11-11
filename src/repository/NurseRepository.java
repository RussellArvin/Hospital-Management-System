package repository;

import model.Nurse;
import repository.base.CsvRepository;
import repository.mapper.NurseMapper;

public class NurseRepository extends CsvRepository<Nurse, NurseMapper> {
    private static final String CSV_FILE = "data/nurses.csv";
    private static final String CSV_HEADER = "id,password,salt,name,age,gender,createdAt,updatedAt";

    public NurseRepository() {
        super(CSV_FILE, CSV_HEADER, new NurseMapper());
    }

    @Override
    public Nurse[] findAll() {
        return super.findAll(Nurse.class);
    }
}
