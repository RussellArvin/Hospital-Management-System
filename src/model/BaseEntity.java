package model;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BaseEntity implements Entity {
    protected String id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    protected BaseEntity(){
        this.id = BaseEntity.generateUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    protected BaseEntity(String id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    protected BaseEntity(
        String id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ){
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setUpdatedAt(){
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt(){
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt(){
        return this.updatedAt;
    }

    public static String generateUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @Override
    public abstract String toCsvString();
}