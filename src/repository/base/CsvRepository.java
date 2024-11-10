package repository.base;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.BaseEntity;
import repository.mapper.BaseMapper;

public abstract class CsvRepository<T extends BaseEntity, M extends BaseMapper<T>> {
    protected final CsvFileManager fileManager;
    protected final M mapper;

    protected CsvRepository(String filePath, String header, M mapper) {
        this.fileManager = new CsvFileManager(filePath, header);
        this.mapper = mapper;
    }

    public void save(T entity) {
        T duplicateEntity = this.findOne(entity.getId());
        if(duplicateEntity != null) throw new RuntimeException("Item already exists for ID: " + entity.getId());

        this.fileManager.appendLine(entity.toCsvString());
    }

    public void saveMany(T[] entities){
        for(int i = 0; i < entities.length; i++){
            this.save(entities[i]);
        }
    }

    public T findOne(String id) {
        String line = this.fileManager.readLine(id);
        if(line == null) return null;
        return mapper.fromCsvString(line);
    }

    protected T[] findAll(Class<T> entityClass) {
        List<String> lines = this.fileManager.readAllLines();
        return mapLines(lines, entityClass);
    }

    protected T[] mapLines(List<String> lines, Class<T> entityClass){
        List<T> entities = lines.stream()
        .filter(line -> !line.equals(this.fileManager.getHeader()))
        .map(mapper::fromCsvString)
        .collect(Collectors.toList());
        
        //TODO: handle this
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(entityClass, entities.size());
        return entities.toArray(array);
    }

    public abstract T[] findAll(); 

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
            throw new RuntimeException("Item not found for update: " + entity.getId());
        }
    
        this.fileManager.writeAllLines(updatedLines);
    }

    public void delete(T entity) {
        String id = entity.getId();
        T entityExists = findOne(id);

        if(entityExists == null) {
            throw new RuntimeException("Item not found for deletion: " + id);
        }
        
        try {
            this.fileManager.deleteLine(id);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to delete item: " + e.getMessage());
        }
    }
}