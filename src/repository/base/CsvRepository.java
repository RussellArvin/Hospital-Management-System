package repository.base;

public abstract class CsvRepository<T> {
    protected final CsvFileManager fileManager;

    protected CsvRepository(String filePath, String header){
        this.fileManager = new CsvFileManager(filePath, header);
    }

    public abstract T findOne(String id);
    public abstract void save(T entity);
    public abstract void update(T entity);
}
