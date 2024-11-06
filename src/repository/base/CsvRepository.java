package repository.base;

import java.util.ArrayList;
import java.util.List;

import model.BaseEntity;

public abstract class CsvRepository<T extends BaseEntity> {
    protected final CsvFileManager fileManager;

    protected CsvRepository(String filePath, String header) {
        this.fileManager = new CsvFileManager(filePath, header);
    }

    public abstract T findOne(String id);

    public void save(T entity) {
        this.fileManager.appendLine(entity.toCsvString());
    }

    public void update(T entity) {
        entity.setUpdatedAt();
    
        List<String> lines = this.fileManager.readAllLines();
        boolean found = false;
    
        List<String> updatedLines = new ArrayList<>();
        updatedLines.add(this.fileManager.getHeader());  // Add header
    
        // Update the specific entity line
        for (String line : lines) {
            if (line.startsWith(this.fileManager.getHeader())) continue;
    
            String id = line.split(",")[0];
    
            if (id.equals(entity.getId())) {
                updatedLines.add(entity.toCsvString());
                found = true;
            } else {
                updatedLines.add(line);
            }
        }   // Removed the break statement
    
        if (!found) {
            throw new RuntimeException("Patient not found for update: " + entity.getId());
        }
    
        this.fileManager.writeAllLines(updatedLines);
    }
}