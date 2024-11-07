package model;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import enums.Gender;
import util.PasswordUtil;

public abstract class User extends BaseEntity{
    protected String password;
    protected byte[] salt;
    protected String name;
    protected int age;
    protected Gender gender;
    public User(
        String id, 
        String password,
        byte[] salt,
        String name,
        int age,
        Gender gender
    ){
        super(id);
        this.password = password;
        this.salt = salt;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public User(
        String id,
        String password,
        byte[] salt,
        String name,
        int age,    
        Gender gender,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ){
        super(id, createdAt, updatedAt);
        this.salt = salt;
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }
    
    public String getName(){
        return name;
    }

    public int getAge(){
        return this.age;
    }

    public Gender getGender(){
        return this.gender;
    }

    public boolean validatePassword(String password){
        try {
            return PasswordUtil.verifyPassword(password, this.password, this.salt);
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public String toCsvString() {
        return String.join(",", 
            id, 
            password, 
            new String(salt, StandardCharsets.UTF_8), // from salt
            name,
            createdAt.toString(), 
            updatedAt.toString()
        );
    }
}