package model;

import java.time.LocalDateTime;

import enums.Gender;

public class Pharmacist extends RegularUser {
    public Pharmacist(
        String id,
        String password,
        byte[] salt,
        String name,
        int age,    
        Gender gender,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {

        super(id, password, salt, name, age, gender, createdAt, updatedAt);
    }
}
