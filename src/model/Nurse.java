package model;

import java.time.LocalDateTime;

import enums.Gender;

public class Nurse extends User {
    public Nurse(
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
