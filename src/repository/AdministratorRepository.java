package repository;

import java.util.List;

import model.Administrator;
import repository.base.CsvRepository;
import repository.mapper.AdministratorMapper;

public class AdministratorRepository extends CsvRepository<Administrator> {
    private static final String CSV_FILE = "data/administrators.csv";
    private static final String CSV_HEADER = "id,password,salt,name,createdAt,updatedAt";

    public AdministratorRepository() {
        super(CSV_FILE, CSV_HEADER);
    }

    public Administrator findOne(String id) {
        String line = this.fileManager.readLine(id);
        if(line == null) return null;
        return AdministratorMapper.fromCsvString(line);
    }

    public Administrator[] findAll(){
        List<String> lines = this.fileManager.readAllLines();
        return lines.stream()
               .map(AdministratorMapper::fromCsvString)
               .toArray(Administrator[]::new);
    }
}


