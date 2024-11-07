package repository;

import model.Administrator;
import repository.base.CsvRepository;
import repository.mapper.AdministratorMapper;

// AdministratorRepository
public class AdministratorRepository extends CsvRepository<Administrator, AdministratorMapper> {
    private static final String CSV_FILE = "data/administrators.csv";
    private static final String CSV_HEADER = "id,password,salt,name,age,gendercreatedAt,updatedAt";

    public AdministratorRepository() {
        super(CSV_FILE, CSV_HEADER, new AdministratorMapper());
    }

    @Override
    public Administrator[] findAll() {
        return super.findAll(Administrator.class);
    }
}