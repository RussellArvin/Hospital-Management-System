package model;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import enums.Gender;
import util.PasswordUtil;

public abstract class User extends BaseEntity{
    protected String password;
    protected byte[] salt;
    protected String name;
    public User(
        String id, 
        String password,
        byte[] salt,
        String name
    ){
        super(id);
        this.password = password;
        this.salt = salt;
        this.name = name;
    }

    public User(
        String id,
        String password,
        byte[] salt,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ){
        super(id, createdAt, updatedAt);
        this.salt = salt;
        this.password = password;
        this.name = name;
    }
    
    public String getName(){
        return name;
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