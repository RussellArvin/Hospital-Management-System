package model;

import java.time.LocalDateTime;

import enums.Gender;

public abstract class User extends BaseEntity{
    protected String password;
    protected String name;
    public User(
        String id, 
        String password,
        String name
    ){
        super(id);
        this.password = password;
        this.name = name;
    }

    public User(
        String id,
        String password,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ){
        super(id, createdAt, updatedAt);
        this.password = password;
        this.name = name;
    }
    
    public String getName(){
        return name;
    }

    public boolean validatePassword(String password){
        return this.password.equals(password);
    }
    
    @Override
    public String toCsvString() {
        return String.join(",", 
            id, 
            password, 
            name,
            createdAt.toString(), 
            updatedAt.toString()
        );
    }
}