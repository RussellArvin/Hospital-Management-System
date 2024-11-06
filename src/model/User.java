package model;

import java.time.LocalDateTime;

import enums.Gender;

public abstract class User extends BaseEntity{
    protected String password;
    protected String name;
    protected int age;
    protected Gender gender;

    public User(
        String id, 
        String password,
        String name,
        int age,
        Gender gender
    ){
        super(id);
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public User(
        String id,
        String password,
        String name,
        int age,    
        Gender gender,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ){
        super(id, createdAt, updatedAt);
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
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
            String.valueOf(age), 
            gender.toString(),
            createdAt.toString(), 
            updatedAt.toString()
        );
    }
}