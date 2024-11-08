package repository;

import model.Medicine;
import repository.base.CsvRepository;
import repository.mapper.MedicineMapper;

public class MedicineRepository extends CsvRepository<Medicine,MedicineMapper> {
    private static final String CSV_FILE = "data/medicine.csv";
    private static final String CSV_HEADER = "id,name,stock,lowStockAlert,createdAt,updatedAt";

    public MedicineRepository(){
        super(CSV_FILE,CSV_HEADER, new MedicineMapper());
    }

    @Override
    public Medicine[] findAll(){
        return super.findAll(Medicine.class);
    }

    public Medicine findOneByName(String name){
        String line = this.fileManager.findLineByColumnValue("name", name);
        return mapper.fromCsvString(line);
    }
}
