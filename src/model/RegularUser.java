package model;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import enums.Gender;

public abstract class RegularUser extends User {
    protected int age;
    protected Gender gender;

    public RegularUser(
        String id, 
        String password,
        byte[] salt,
        String name,
        int age,
        Gender gender
    ){
        super(id,password,salt,name);
        this.age = age;
        this.gender = gender;
    }

    public RegularUser(
        String id,
        String password,
        byte[] salt,
        String name,
        int age,    
        Gender gender,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {

        super(id, password, salt, name, createdAt, updatedAt);
        this.age = age;
        this.gender = gender;
    }

    @Override
    public String toCsvString() {
        return String.join(",", 
            id, 
            password,
            new String(salt, StandardCharsets.UTF_8), // from salt
            name,
            String.valueOf(age),
            gender.toString(),
            createdAt.toString(), 
            updatedAt.toString()
        );
    }
}
